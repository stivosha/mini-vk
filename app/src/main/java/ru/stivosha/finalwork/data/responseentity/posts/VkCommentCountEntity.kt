package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkCommentCountEntity(
    @SerializedName("count")
    val count: Int
)