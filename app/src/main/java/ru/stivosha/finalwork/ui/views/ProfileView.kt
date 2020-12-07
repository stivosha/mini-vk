package ru.stivosha.finalwork.ui.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.ProfileDetailObject
import ru.stivosha.finalwork.data.entity.User

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProfileView : MvpView {
    fun showProgress()
    fun onBindProfile(user: User)
    fun onBindPosts(posts: List<ProfileDetailObject>)
    fun hideProgress()
    fun showErrorToast(text: String)
    fun showResultToast(text: String)
    fun showInternetConnectionErrorBlock()
    fun hideInternetConnectionErrorBlock()
}