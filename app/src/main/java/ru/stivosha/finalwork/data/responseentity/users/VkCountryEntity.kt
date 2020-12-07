package ru.stivosha.finalwork.data.responseentity.users

import com.google.gson.annotations.SerializedName

class VkCountryEntity(
    @SerializedName("id")
    val countryId: Int,
    @SerializedName("title")
    val title: String,
)