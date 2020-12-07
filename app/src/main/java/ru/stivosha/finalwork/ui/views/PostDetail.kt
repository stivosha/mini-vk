package ru.stivosha.finalwork.ui.views

import android.content.Intent
import com.arellomobile.mvp.MvpView
import ru.stivosha.finalwork.data.entity.PostDetailObject

interface PostDetail : MvpView {
    fun openShareImageDialog(action: Intent)
    fun setComments(comments: List<PostDetailObject>)
    fun showToast(text: String)
    fun showToast(resourceId: Int)
    fun showInternetConnectionErrorBlock()
    fun hideInternetConnectionErrorBlock()
}