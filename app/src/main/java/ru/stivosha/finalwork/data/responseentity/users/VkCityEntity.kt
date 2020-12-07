package ru.stivosha.finalwork.data.responseentity.users

import com.google.gson.annotations.SerializedName

class VkCityEntity(
    @SerializedName("id")
    val cityId: Int,
    @SerializedName("title")
    val title: String,
)