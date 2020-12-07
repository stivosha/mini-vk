package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkLikeEntity(
    @SerializedName("count")
    val count: Int,
    @SerializedName("user_likes")
    val userLikes: Int,
)