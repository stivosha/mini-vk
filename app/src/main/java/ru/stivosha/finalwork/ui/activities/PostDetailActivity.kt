package ru.stivosha.finalwork.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.post_detail_post_element.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.Colors
import ru.stivosha.finalwork.data.consts.Errors
import ru.stivosha.finalwork.data.consts.Extras
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.PostDetailObject
import ru.stivosha.finalwork.presentation.presenter.PostDetailPresenterImpl
import ru.stivosha.finalwork.ui.recycler.adapters.CommentListAdapter
import ru.stivosha.finalwork.ui.views.PostDetail

class PostDetailActivity : MvpAppCompatActivity(), PostDetail {

    @InjectPresenter
    lateinit var presenter: PostDetailPresenterImpl
    private lateinit var post: Post
    private val adapter = CommentListAdapter()
    private var buttonSendActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        supportActionBar?.setTitle(R.string.activityDetailTittle)
        post = intent.extras!!.getParcelable(Extras.POST_ITEM)!!
        initCommentSendBlock()
        init()
        presenter.loadComments(post)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.postDetailToolBarMenuShare -> {
                if (post.imageContentUrl == null) {
                    showToast(getString(R.string.noImageToShare))
                } else {
                    presenter.shareImage(cacheDir, this, (postDetailImageContent.drawable as BitmapDrawable).bitmap, post)
                }
            }
            R.id.postDetailToolBarMenuSave -> {
                if (post.imageContentUrl == null) {
                    showToast(getString(R.string.noImageToSave))
                } else {
                    if (isExternalStorageWriteable()) {
                        try {
                            presenter.saveInGallery(this.contentResolver, (postDetailImageContent.drawable as BitmapDrawable).bitmap, post)
                        } catch (e: Exception) {
                            Log.d(this::class.java.simpleName, e.localizedMessage ?: Errors.UNKNOWN_ERROR)
                            showToast(getString(R.string.error))
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
                        } else {
                            presenter.saveInGallery(this.contentResolver, (postDetailImageContent.drawable as BitmapDrawable).bitmap, post)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    presenter.saveInGallery(this.contentResolver, (postDetailImageContent.drawable as BitmapDrawable).bitmap, post)
                } else {
                    showToast(getString(R.string.needPermission))
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initCommentSendBlock() {
        postDetailCommentSmile.setOnClickListener { showToast(getString(R.string.smileIconClicked)) }
        postDetailCommentAttach.setOnClickListener { showToast(getString(R.string.attachIconClicked)) }
        postDetailCommentEditText.addTextChangedListener {
            if (buttonSendActive && (it.isNullOrEmpty())) {
                postDetailCommentSend.setImageResource(R.drawable.ic_send_36)
                buttonSendActive = false
            } else if (!buttonSendActive && !(it.isNullOrEmpty())) {
                postDetailCommentSend.setImageResource(R.drawable.ic_send_active_36)
                buttonSendActive = true
            }
        }
        postDetailCommentSend.setOnClickListener {
            if (!buttonSendActive) {
                showToast(getString(R.string.textFieldCantBeEmpty))
                return@setOnClickListener
            }
            presenter.sendComment(post, postDetailCommentEditText.text.toString())
            postDetailCommentEditText.text.clear()
        }
    }

    private fun init() {
        activityPostDetailCommentsList.adapter = adapter
        postDetailViewRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor(Colors.COLOR_PRIMARY))
        postDetailViewRefresh.setColorSchemeColors(Color.WHITE)
        postDetailViewRefresh.setOnRefreshListener {
            if (postDetailErrorInternetConnectionBlock.isVisible)
                hideInternetConnectionErrorBlock()
            presenter.loadComments(post)
        }
    }

    private fun isExternalStorageWriteable() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun openShareImageDialog(action: Intent) {
        startActivity(Intent.createChooser(action, getString(R.string.share)))
    }

    override fun setComments(comments: List<PostDetailObject>) {
        adapter.submitList(comments)
        postDetailViewRefresh.isRefreshing = false
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(resourceId: Int) {
        Toast.makeText(this, getString(resourceId), Toast.LENGTH_SHORT).show()
    }

    override fun showInternetConnectionErrorBlock() {
        postDetailViewRefresh.isRefreshing = false
        postDetailErrorInternetConnectionBlock.isVisible = true
    }

    override fun hideInternetConnectionErrorBlock() {
        postDetailErrorInternetConnectionBlock.isGone = true
    }

    companion object {

        private const val EXTRA_POST_ITEM = "postItem"
        private const val PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 11

        fun createIntent(context: Context, post: Post): Intent =
            Intent(context, PostDetailActivity::class.java)
                .putExtra(EXTRA_POST_ITEM, post)
    }
}