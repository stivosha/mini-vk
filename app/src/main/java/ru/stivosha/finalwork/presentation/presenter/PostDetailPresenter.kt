package ru.stivosha.finalwork.presentation.presenter

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import ru.stivosha.finalwork.data.entity.Post
import java.io.File

interface PostDetailPresenter {
    fun sendComment(post: Post, message: String)
    fun loadComments(post: Post)
    fun saveInGallery(contentResolver: ContentResolver, bitmap: Bitmap, post: Post)
    fun shareImage(cacheDir: File, context: Context, bitmap: Bitmap, post: Post)
}