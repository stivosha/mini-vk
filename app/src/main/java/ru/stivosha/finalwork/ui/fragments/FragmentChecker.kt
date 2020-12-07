package ru.stivosha.finalwork.ui.fragments

import android.widget.ImageView
import ru.stivosha.finalwork.data.entity.Post

interface FragmentChecker {
    fun updateFavoritePageState(isLikedItemExist: Boolean)
    fun onPostItemClick(pos: Int, postItem: Post, sharedImageView: ImageView?)
}