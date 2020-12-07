package ru.stivosha.finalwork.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Comment(
    override val id: Int,
    val authorName: String,
    val textContent: String?,
    val dateCreation: Date,
    val authorImageUrl: String? = null,
) : Parcelable, PostDetailObject