package ru.stivosha.finalwork.ui.fragments

import android.widget.ImageView
import ru.stivosha.finalwork.data.entity.Post

interface PostItemClickListener {
    fun onPostItemClick(pos: Int, postItem: Post, sharedImageView: ImageView?)
}