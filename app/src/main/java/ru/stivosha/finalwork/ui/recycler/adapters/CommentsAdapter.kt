package ru.stivosha.finalwork.ui.recycler.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comments_element_layout.*
import kotlinx.android.synthetic.main.post_detail_post_element.*
import kotlinx.android.synthetic.main.post_detail_post_element.view.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.ViewType
import ru.stivosha.finalwork.data.entity.Comment
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.data.entity.PostDetailObject
import ru.stivosha.finalwork.helpers.DateHelper
import java.util.*


class CommentListAdapter : RecyclerView.Adapter<BaseViewHolder<PostDetailObject>>() {
    private val differ: AsyncListDiffer<PostDetailObject> = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<PostDetailObject>) {
        differ.submitList(list)
    }

    override fun getItemViewType(position: Int) = if (position == 0) ViewType.POST_ELEMENT else ViewType.COMMENT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PostDetailObject> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.POST_ELEMENT -> PostElementDetailViewHolder(inflater.inflate(R.layout.post_detail_post_element, parent, false))
            ViewType.COMMENT -> CommentViewHolder(inflater.inflate(R.layout.comments_element_layout, parent, false))
            else -> CommentViewHolder(inflater.inflate(R.layout.comments_element_layout, parent, false))
        }
    }

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: BaseViewHolder<PostDetailObject>, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolder<PostDetailObject>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val diff = payloads[0] as Bundle
            val wordComments = holder.itemView.postDetailCommentsCount.text.toString().split(" ")[1]
            holder.itemView.postDetailCommentsCount.text = ("${diff.getInt(COMMENTS_COUNT)} " + wordComments)
        }
    }

    companion object {

        const val COMMENTS_COUNT = "commentsCount"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostDetailObject>() {
            override fun areItemsTheSame(oldItem: PostDetailObject, newItem: PostDetailObject): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PostDetailObject, newItem: PostDetailObject): Boolean {
                if (oldItem is Comment && newItem is Comment)
                    return oldItem.textContent == newItem.textContent
                if (oldItem is Post && newItem is Post)
                    return oldItem.textContent == newItem.textContent
                            && oldItem.imageContentUrl == newItem.imageContentUrl
                            && oldItem.commentsCount == newItem.commentsCount
                return false
            }

            override fun getChangePayload(oldItem: PostDetailObject, newItem: PostDetailObject): Any? {
                val diff = Bundle()
                if (oldItem is Post && newItem is Post) {
                    if (oldItem.commentsCount != newItem.commentsCount)
                        diff.putInt(COMMENTS_COUNT, newItem.commentsCount)
                }
                return diff
            }
        }
    }
}

class CommentViewHolder(override val containerView: View) : BaseViewHolder<PostDetailObject>(containerView) {
    override fun bind(item: PostDetailObject) {
        val itemComment = item as Comment
        Glide.with(containerView).load(itemComment.authorImageUrl).into(commentAuthorImage)
        commentAuthorName.text = itemComment.authorName
        commentTextContent.text = itemComment.textContent
        commentDateCreation.text = DateHelper.timeToString(itemComment.dateCreation, Date())
    }
}

class PostElementDetailViewHolder(override val containerView: View) : BaseViewHolder<PostDetailObject>(containerView) {
    override fun bind(item: PostDetailObject) {
        val itemPost = item as Post
        if (itemPost.textContent.isNotEmpty()) {
            postDetailTextContent.text = itemPost.textContent
            postDetailTextContent.isVisible = true
        }
        postViewDetailAuthor.text = itemPost.author
        postDetailLikesCount.text = postDetailLikesCount.text.toString().format(itemPost.likesCount)
        postDetailCommentsCount.text = postDetailCommentsCount.text.toString().format(itemPost.commentsCount)
        Glide.with(containerView).load(itemPost.authorImageUrl).into(postViewDetailAvatar)
        Glide.with(containerView).load(itemPost.imageContentUrl).into(postDetailImageContent)
        postDetailLikeButton.setImageResource(if (itemPost.isLiked) R.drawable.ic_heart_active else R.drawable.ic_heart_not_active)
    }
}