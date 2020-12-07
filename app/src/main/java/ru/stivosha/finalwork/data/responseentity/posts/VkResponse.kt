package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkResponse(
    @SerializedName("response")
    val response: VkPostsResponse
)