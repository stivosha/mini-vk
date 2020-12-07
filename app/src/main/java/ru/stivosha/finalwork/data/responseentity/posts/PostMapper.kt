package ru.stivosha.finalwork.data.responseentity.posts

import ru.stivosha.finalwork.data.consts.StringConst
import ru.stivosha.finalwork.data.entity.Post
import java.util.*

class PostMapper(postResponseEntity: VkPostEntity, groups: Map<Int, Pair<String, String>>, profiles: Map<Int, Pair<String, String>>) {

    val post: Post

    init {
        var ownerId = postResponseEntity.sourceId ?: postResponseEntity.fromId!!
        var postId = postResponseEntity.postId ?: postResponseEntity.id!!
        val author = if (ownerId > 0) profiles[ownerId] else groups[ownerId * -1]
        val photoAttachment = postResponseEntity.attachments?.firstOrNull { it.type == "photo" }
        var url: String? = null
        if (photoAttachment != null) {
            url = photoAttachment.photo.sizes.firstOrNull { it.type == "z" }?.url
        } else if (postResponseEntity.photo != null) {
            url = postResponseEntity.photo.sizes.firstOrNull { it.type == "z" }?.url
        }
        post = Post(
            id = postId,
            author = author?.first ?: StringConst.UNKNOWN_AUTHOR,
            authorId = ownerId,
            dateCreation = Date(postResponseEntity.date * 1000),
            textContent = postResponseEntity.textContent ?: "",
            authorImageUrl = author?.second,
            imageContentUrl = url,
            likesCount = postResponseEntity.likeEntity!!.count,
            commentsCount = postResponseEntity.commentCountEntity!!.count,
            repostsCount = postResponseEntity.repostEntity!!.count,
            isLiked = postResponseEntity.likeEntity.userLikes == 1
        )
    }
}