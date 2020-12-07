package ru.stivosha.finalwork.data.responseentity.posts

import com.google.gson.annotations.SerializedName

class VkPostsResponse(
    @SerializedName("items")
    val posts: List<VkPostEntity>,
    @SerializedName("profiles")
    val profiles: List<VkProfileEntity>,
    @SerializedName("groups")
    val groups: List<VkGroupEntity>
)