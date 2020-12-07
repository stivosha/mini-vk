package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkPhotoSizeEntity(
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String,
)