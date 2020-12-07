package ru.stivosha.finalwork.presentation.presenter

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import ru.stivosha.finalwork.BuildConfig
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.Errors
import ru.stivosha.finalwork.data.consts.StringConst
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.ui.views.PostDetail
import java.io.File
import java.io.FileOutputStream

@InjectViewState
class PostDetailPresenterImpl : MvpPresenter<PostDetail>(), PostDetailPresenter {

    private val compositeDispose = CompositeDisposable()
    private val repository = SimpleDi.repository

    override fun saveInGallery(contentResolver: ContentResolver, bitmap: Bitmap, post: Post) {
        MediaStore.Images.Media.insertImage(contentResolver, bitmap, post.id.toString(), "")
        viewState.showToast(R.string.success)
    }

    override fun shareImage(cacheDir: File, context: Context, bitmap: Bitmap, post: Post) {
        val file = saveImageInStorage(cacheDir, bitmap, post.id)
        val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/jpeg"
        }
        viewState.openShareImageDialog(sendIntent)
    }

    private fun saveImageInStorage(cacheDir: File, bitmap: Bitmap, postId: Int?): File {
        val cachedFile = File(cacheDir, "${postId}.jpg")
        cachedFile.createNewFile()
        val ostream = FileOutputStream(cachedFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
        ostream.flush()
        ostream.close()
        return cachedFile
    }

    override fun sendComment(post: Post, message: String) {
        val dispose = repository.sendComment(post, message)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    viewState.showToast(StringConst.SUCCESS)
                    val copyPost = post.copy()
                    copyPost.commentsCount += 1
                    loadComments(copyPost)
                },
                onError = {
                    viewState.showToast(it.localizedMessage ?: Errors.UNKNOWN_ERROR)
                }
            )
        compositeDispose.add(dispose)
    }

    override fun loadComments(post: Post) {
        val dispose = repository.loadComments(post)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val newList = it.toMutableList()
                    newList.add(0, post)
                    viewState.setComments(newList)
                },
                onError = {
                    viewState.showInternetConnectionErrorBlock()
                }
            )
        compositeDispose.add(dispose)
    }

    override fun detachView(view: PostDetail?) {
        super.detachView(view)
        compositeDispose.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDispose.dispose()
    }
}