package ru.stivosha.finalwork.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.stivosha.finalwork.data.consts.Errors
import ru.stivosha.finalwork.data.consts.StringConst
import ru.stivosha.finalwork.data.entity.User
import ru.stivosha.finalwork.data.responseentity.posts.PostMapper
import ru.stivosha.finalwork.data.services.VkService
import ru.stivosha.finalwork.di.SimpleDi
import ru.stivosha.finalwork.ui.views.ProfileView
import java.util.concurrent.TimeUnit

@InjectViewState
class ProfilePresenterImpl : MvpPresenter<ProfileView>(), ProfilePresenter {
    private val compositeDispose = CompositeDisposable()
    private val repository = SimpleDi.repository

    override fun loadProfileAndComments(profileId: Int) {
        val dispose = repository.loadProfile(profileId = profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    viewState.onBindProfile(it)
                    loadPosts(profileId)
                },
                onError = {
                    viewState.showInternetConnectionErrorBlock()
                }
            )
        compositeDispose.add(dispose)
    }

    override fun loadPosts(profileId: Int) {
        val dispose = repository.loadPostsFrom(profileId = profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    viewState.onBindPosts(it)
                },
                onError = {
                    viewState.showInternetConnectionErrorBlock()
                }
            )
        compositeDispose.add(dispose)
    }

    override fun createPost(profileId: Int, text: String) {
        val dispose = repository.createPost(profileId, text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    viewState.showResultToast(StringConst.SUCCESS)
                    loadPosts(profileId)
                },
                onError = {
                    viewState.showErrorToast(Errors.INTERNET_ERROR)
                }
            )
        compositeDispose.add(dispose)
    }

    override fun detachView(view: ProfileView?) {
        super.detachView(view)
        compositeDispose.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDispose.dispose()
    }
}