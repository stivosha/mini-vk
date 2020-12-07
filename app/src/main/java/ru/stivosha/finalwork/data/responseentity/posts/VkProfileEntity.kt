package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkProfileEntity(
    @SerializedName("id")
    val profileId: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_100")
    val photoUrl: String,
)