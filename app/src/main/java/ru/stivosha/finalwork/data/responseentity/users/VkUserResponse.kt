package ru.stivosha.finalwork.data.responseentity.users

import com.google.gson.annotations.SerializedName

class VkUserResponse(
    @SerializedName("response")
    val users: List<VkUserEntity>
)