package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkGroupEntity(
    @SerializedName("id")
    val groupId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo_100")
    val photoUrl: String,
)