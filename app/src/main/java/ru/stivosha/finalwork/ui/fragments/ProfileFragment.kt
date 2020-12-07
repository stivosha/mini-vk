package ru.stivosha.finalwork.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.Colors
import ru.stivosha.finalwork.data.consts.Errors
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.ProfileDetailObject
import ru.stivosha.finalwork.data.entity.User
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.presentation.presenter.ProfilePresenterImpl
import ru.stivosha.finalwork.ui.activities.activityForResult.NewPostActivity
import ru.stivosha.finalwork.ui.recycler.adapters.ProfileAdapter
import ru.stivosha.finalwork.ui.views.ProfileView

class ProfileFragment : MvpAppCompatFragment(), ProfileView {

    @InjectPresenter
    lateinit var presenter: ProfilePresenterImpl
    lateinit var adapter: ProfileAdapter
    private var fragmentChecker: FragmentChecker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChecker)
            fragmentChecker = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        presenter.loadProfileAndComments(SimpleDi.userId!!.toInt())
    }

    private fun initRecycler() {
        showProgress()
        adapter = ProfileAdapter(
            onNewPostButtonClickedListener,
            onPostClickedListener,
            onSeeMoreButtonClickedListener,
            onAttachPhotoButtonClickedListener,
            onEditProfileButtonClickedListener
        )
        profileFragmentElementsList.adapter = adapter
        profileFragmentElementsList.addItemDecoration(DividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL))
        profileFragmentViewRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor(Colors.COLOR_PRIMARY))
        profileFragmentViewRefresh.setColorSchemeColors(Color.WHITE)
        profileFragmentViewRefresh.setOnRefreshListener {
            if (profileFragmentErrorInternetConnectionBlock.isVisible)
                hideInternetConnectionErrorBlock()
            presenter.loadProfileAndComments(SimpleDi.userId!!.toInt())
        }
    }

    override fun showProgress() {
        profileFragmentShimmerViewContainer.isVisible = true
        profileFragmentShimmerViewContainer.startShimmer()
    }

    override fun hideProgress() {
        profileFragmentShimmerViewContainer.stopShimmer()
        profileFragmentShimmerViewContainer.isGone = true
        profileFragmentViewRefresh.isRefreshing = false
    }

    override fun onBindProfile(user: User) {
        adapter.submitList(listOf(user))
        hideProgress()
    }

    override fun onBindPosts(posts: List<ProfileDetailObject>) {
        val profileElement = adapter.getProfile()
        val mutableList = posts.toMutableList()
        mutableList.add(0, profileElement)
        adapter.submitList(mutableList)
    }

    override fun showErrorToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun showResultToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    override fun showInternetConnectionErrorBlock() {
        hideProgress()
        if (adapter.itemCount != 0) {
            showErrorToast(Errors.INTERNET_ERROR)
        } else {
            profileFragmentErrorInternetConnectionBlock.isVisible = true
        }
    }

    override fun hideInternetConnectionErrorBlock() {
        profileFragmentErrorInternetConnectionBlock.isGone = true
        showProgress()
    }

    private val onNewPostButtonClickedListener: ((ProfileDetailObject, Int) -> Unit) = { profile, pos ->
        startForResult.launch(NewPostActivity.createIntent(this.requireContext()))
    }

    private val onPostClickedListener: ((ProfileDetailObject, Int) -> Unit) = { post, pos ->
        fragmentChecker?.onPostItemClick(pos, post as Post, null)
    }

    private val onSeeMoreButtonClickedListener: (() -> Unit) = {
        showResultToast(getString(R.string.seeMoreButtonClicked))
    }

    private val onAttachPhotoButtonClickedListener: (() -> Unit) = {
        showResultToast(getString(R.string.attachIconClicked))
    }

    private val onEditProfileButtonClickedListener: (() -> Unit) = {
        showResultToast(getString(R.string.editProfileButtonClicked))
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            NEW_POST_CREATED_OK -> {
                presenter.createPost(SimpleDi.userId!!.toInt(), result.data!!.getStringExtra(POST_TEXT)!!)
            }
        }
    }

    companion object {
        const val NEW_POST_CREATED_OK = 1
        const val POST_TEXT = "postText"
        fun createInstance(): Fragment = ProfileFragment()
    }
}