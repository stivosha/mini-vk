package ru.stivosha.finalwork.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_post_list.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.VkClientApplication
import ru.stivosha.finalwork.data.consts.Colors
import ru.stivosha.finalwork.data.consts.Tags.ERROR_DIALOG_TAG
import ru.stivosha.finalwork.data.db.PostDataBase
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.TestObject
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.presentation.presenter.PostPresenter
import ru.stivosha.finalwork.presentation.presenter.PostPresenterImpl
import ru.stivosha.finalwork.ui.activities.MainActivity
import ru.stivosha.finalwork.ui.recycler.DateItemDecorator
import ru.stivosha.finalwork.ui.recycler.DiffCallback
import ru.stivosha.finalwork.ui.recycler.PostItemTouchHelperCallback
import ru.stivosha.finalwork.ui.recycler.adapters.PostListAdapter
import ru.stivosha.finalwork.ui.views.PostListView
import javax.inject.Inject

class PostListFragment : MvpAppCompatFragment(), PostListView, PostItemClickListener {

    @InjectPresenter
    lateinit var presenter: PostPresenterImpl

    private var fragmentChecker: FragmentChecker? = null
    private lateinit var adapter: PostListAdapter
    private val compositeDispose = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChecker)
            fragmentChecker = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        if (savedInstanceState == null)
            presenter.loadPosts()
    }

    private fun initRecycler() {
        adapter = PostListAdapter(
            onPostLikedListener,
            onPostDislikedListener,
            onPostDismissListener,
            this
        )
        fragmentPostListRecyclerView.adapter = adapter
        val callback = PostItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(fragmentPostListRecyclerView)
        fragmentPostListRecyclerView.addItemDecoration(DateItemDecorator(fragmentPostListRecyclerView, R.layout.date_item_decorator))
        fragmentPostListRecyclerView.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        postListRecyclerViewRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor(Colors.COLOR_PRIMARY))
        postListRecyclerViewRefresh.setColorSchemeColors(Color.WHITE)
        postListRecyclerViewRefresh.setOnRefreshListener {
            presenter.loadPosts()
        }
    }

    override fun updateFavoritePageState() {
        fragmentChecker?.updateFavoritePageState(adapter.posts.any { it.isLiked })
    }

    override fun onPostItemClick(pos: Int, postItem: Post, sharedImageView: ImageView?) {
        fragmentChecker?.onPostItemClick(pos, postItem, sharedImageView)
    }

    private val onPostDismissListener: ((Post, Int) -> Unit) = { post, _ ->
        presenter.hidePost(post)
        updateFavoritePageState()
    }

    private val onPostLikedListener: ((Post, Int) -> Unit) = { post, position ->
        presenter.likePost(post, position)
        updateFavoritePageState()
    }

    private val onPostDislikedListener: ((Post, Int) -> Unit) = { post, position ->
        presenter.dislikePost(post, position)
        updateFavoritePageState()
    }

    override fun showProgress() {
        shimmerViewContainer.isVisible = true
        shimmerViewContainer.startShimmer()
    }

    override fun hideProgress() {
        shimmerViewContainer.stopShimmer()
        shimmerViewContainer.isGone = true
        postListRecyclerViewRefresh.isRefreshing = false
    }

    override fun showErrorToast(text: String) {
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorDialog(text: String) {
        val frag = ErrorDialogFragment.createInstance(text)
        frag.show(parentFragmentManager, ERROR_DIALOG_TAG)
    }

    override fun setPostList(posts: List<Post>) {
        val dispose = Single.fromCallable {
            val callback = DiffCallback(adapter.posts, posts)
            val result = DiffUtil.calculateDiff(callback)
            result
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = { result ->
                adapter.updatePosts(posts)
                result.dispatchUpdatesTo(adapter)
                updateFavoritePageState()
                hideProgress()
            })
        compositeDispose.add(dispose)
    }

    override fun updateElementInList(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun onStop() {
        super.onStop()
        compositeDispose.clear()
    }

    companion object {
        fun createInstance(): Fragment = PostListFragment()
    }
}