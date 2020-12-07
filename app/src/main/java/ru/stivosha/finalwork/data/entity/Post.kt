package ru.stivosha.finalwork.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    override val id: Int,
    val author: String,
    val textContent: String,
    val dateCreation: Date,
    var dateDiff: String? = null,
    val authorImageUrl: String? = null,
    val imageContentUrl: String? = null,
    var isLiked: Boolean = false,
    var likesCount: Int = 0,
    var commentsCount: Int = 0,
    var repostsCount: Int = 0,
    val authorId: Int = 0,
) : Parcelable, PostDetailObject, ProfileDetailObject