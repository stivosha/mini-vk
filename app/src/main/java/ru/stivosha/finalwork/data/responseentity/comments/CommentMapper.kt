package ru.stivosha.finalwork.data.responseentity.comments

import ru.stivosha.finalwork.data.consts.StringConst
import ru.stivosha.finalwork.data.entity.Comment
import java.util.*

class CommentMapper(commentEntity: VkCommentEntity, groups: Map<Int, Pair<String, String>>, profiles: Map<Int, Pair<String, String>>) {
    val comment: Comment

    init {
        val author = if (commentEntity.ownerId > 0) profiles[commentEntity.ownerId] else groups[commentEntity.ownerId * -1]
        comment = Comment(
            id = commentEntity.commentId,
            authorName = author?.first ?: StringConst.UNKNOWN_AUTHOR,
            textContent = commentEntity.text,
            dateCreation = Date(commentEntity.dateCreationLong * 1000),
            authorImageUrl = author?.second,
        )
    }
}