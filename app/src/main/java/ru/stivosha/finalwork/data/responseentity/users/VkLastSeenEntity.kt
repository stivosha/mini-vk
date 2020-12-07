package ru.stivosha.finalwork.data.responseentity.users

import com.google.gson.annotations.SerializedName

class VkLastSeenEntity (
    @SerializedName("platform")
    val platformId: Int,
    @SerializedName("time")
    val timeLastSeen: Long,
)