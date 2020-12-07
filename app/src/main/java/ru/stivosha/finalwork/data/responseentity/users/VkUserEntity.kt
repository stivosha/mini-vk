package ru.stivosha.finalwork.data.responseentity.users

import com.google.gson.annotations.SerializedName

class VkUserEntity (
    @SerializedName("id")
    val profileId: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("domain")
    val domain: String,
    @SerializedName("bdate")
    val bdate: String?,
    @SerializedName("country")
    val country: VkCountryEntity?,
    @SerializedName("city")
    val city: VkCityEntity?,
    @SerializedName("photo_max")
    val photoUrl: String,
    @SerializedName("about")
    val about: String?,
    @SerializedName("followers_count")
    val followersCount: Int,
    @SerializedName("university_name")
    val universityName: String?,
    @SerializedName("last_seen")
    val lastSeen: VkLastSeenEntity?,
    @SerializedName("status")
    val status: String?,
)