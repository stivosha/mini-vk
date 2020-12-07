package ru.stivosha.finalwork.presentation.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.stivosha.finalwork.VkClientApplication
import ru.stivosha.finalwork.data.consts.Errors
import ru.stivosha.finalwork.data.db.PostDataBase
import ru.stivosha.finalwork.data.db.dbentity.PostDbEntity
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.responseentity.posts.PostMapper
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.ui.views.PostListView
import java.util.*
import javax.inject.Inject

@InjectViewState
class PostPresenterImpl : MvpPresenter<PostListView>(), PostPresenter {
    private val compositeDispose = CompositeDisposable()
    private val repository = SimpleDi.repository

    override fun loadPosts(isFavoritePosts: Boolean) {
        if (SimpleDi.token == null) {
            viewState.hideProgress()
            return
        }
        val dispose = repository.loadPosts(isFavoritePosts)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.showProgress()
            }
            .subscribeBy(
                onSuccess = { newPosts ->
                    viewState.setPostList(newPosts)
                }, onError = {
                    viewState.hideProgress()
                    viewState.showErrorDialog(it.localizedMessage ?: Errors.UNKNOWN_ERROR)
                }
            )
        compositeDispose.add(dispose)
    }

    override fun likePost(post: Post, position: Int) {
        like(post)
        viewState.updateElementInList(position)
        val dispose = repository.likePost(post, position)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    viewState.showErrorToast(Errors.INTERNET_ERROR)
                    dislike(post)
                    //viewState.updateElementInList(position) // Если обновлять элемент таким способом он просто исчезает, поэтмоу лучше пусть горит активным
                })
        compositeDispose.add(dispose)
    }

    override fun dislikePost(post: Post, position: Int) {
        dislike(post)
        val dispose = repository.dislikePost(post, position)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    like(post)
                    viewState.showErrorToast(Errors.INTERNET_ERROR)
                })
        viewState.updateElementInList(position)
        compositeDispose.add(dispose)
    }

    private fun like(post: Post) {
        post.isLiked = true
        post.likesCount += 1
    }

    private fun dislike(post: Post) {
        post.isLiked = false
        post.likesCount -= 1
    }

    override fun hidePost(post: Post) {
        val dispose = repository.hidePost(post)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    viewState.showErrorToast(Errors.INTERNET_ERROR)
                })
        compositeDispose.add(dispose)
    }

    override fun detachView(view: PostListView?) {
        super.detachView(view)
        compositeDispose.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDispose.dispose()
    }
}