package ru.stivosha.finalwork.data.responseentity.comments

import com.google.gson.annotations.SerializedName

class VkCommentsResponse(
    @SerializedName("response")
    val commentBody: VkCommentsBody?
)