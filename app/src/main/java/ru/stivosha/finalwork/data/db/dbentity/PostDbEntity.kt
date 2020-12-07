package ru.stivosha.finalwork.data.db.dbentity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.stivosha.finalwork.data.entity.Post

@Entity(tableName = "Post")
class PostDbEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "author_name")
    val authorName: String,

    @ColumnInfo(name = "text_content")
    val textContent: String,

    @ColumnInfo(name = "date_creation")
    val dateCreation: Long,

    @ColumnInfo(name = "author_image_url")
    val authorImageUrl: String,

    @ColumnInfo(name = "content_image_url")
    val imageContentUrl: String? = null,

    @ColumnInfo(name = "is_liked")
    var isLiked: Boolean = false,

    @ColumnInfo(name = "likes_count")
    var likesCount: Int = 0,

    @ColumnInfo(name = "comments_count")
    var commentsCount: Int = 0,

    @ColumnInfo(name = "reposts_count")
    var repostsCount: Int = 0,

    @ColumnInfo(name = "author_id")
    val authorId: Int = 0,
) {
    companion object {
        fun create(post: Post) = PostDbEntity(
            post.id,
            post.author,
            post.textContent,
            post.dateCreation.time,
            post.authorImageUrl!!,
            post.imageContentUrl,
            post.isLiked,
            post.likesCount,
            post.commentsCount,
            post.repostsCount,
            post.authorId
        )
    }
}