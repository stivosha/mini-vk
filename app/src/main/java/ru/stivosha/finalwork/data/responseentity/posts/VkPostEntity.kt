package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkPostEntity(
    @SerializedName("source_id")
    val sourceId: Int?,
    @SerializedName("from_id")
    val fromId: Int?,
    @SerializedName("post_id")
    val postId: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val textContent: String?,
    @SerializedName("attachments")
    val attachments: List<VkAttachment>?,
    @SerializedName("photo")
    val photo: VkPhotoEntity?,
    @SerializedName("likes")
    val likeEntity: VkLikeEntity?,
    @SerializedName("comments")
    val commentCountEntity: VkCommentCountEntity?,
    @SerializedName("reposts")
    val repostEntity: VkRepostEntity?,
)