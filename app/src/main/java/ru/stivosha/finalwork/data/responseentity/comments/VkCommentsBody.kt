package ru.stivosha.finalwork.data.responseentity.comments

import com.google.gson.annotations.SerializedName
import ru.stivosha.finalwork.data.responseentity.posts.VkGroupEntity
import ru.stivosha.finalwork.data.responseentity.posts.VkProfileEntity

class VkCommentsBody(
    @SerializedName("count")
    val count: Int,
    @SerializedName("items")
    val comments: List<VkCommentEntity>,
    @SerializedName("profiles")
    val profiles: List<VkProfileEntity>,
    @SerializedName("groups")
    val groups: List<VkGroupEntity>
)