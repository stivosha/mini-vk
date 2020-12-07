package ru.stivosha.finalwork.ui.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.stivosha.finalwork.data.entity.Post

@StateStrategyType(AddToEndSingleStrategy::class)
interface PostListView : MvpView {
    fun showProgress()
    fun hideProgress()
    fun showErrorToast(text: String)
    fun showErrorDialog(text: String)
    fun setPostList(posts: List<Post>)
    fun updateElementInList(position: Int)
    fun updateFavoritePageState()
}