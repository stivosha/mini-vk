package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkAttachment(
    @SerializedName("type")
    val type: String,
    @SerializedName("photo")
    val photo: VkPhotoEntity
)