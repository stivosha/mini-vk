package ru.stivosha.finalwork.data.responseentity.comments

import com.google.gson.annotations.SerializedName

class VkCommentEntity(
    @SerializedName("id")
    val commentId: Int,
    @SerializedName("from_id")
    val ownerId: Int,
    @SerializedName("date")
    val dateCreationLong: Long,
    @SerializedName("text")
    val text: String?
)