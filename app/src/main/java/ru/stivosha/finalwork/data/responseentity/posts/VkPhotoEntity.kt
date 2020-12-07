package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkPhotoEntity(
    @SerializedName("sizes")
    val sizes: List<VkPhotoSizeEntity>
)