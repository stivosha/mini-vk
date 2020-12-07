package ru.stivosha.finalwork.ui.recycler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.post_view.*
import kotlinx.android.synthetic.main.post_view_layout.*
import kotlinx.android.synthetic.main.post_view_layout.myPostViewTextContent
import kotlinx.android.synthetic.main.post_view_only_text_layout.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.DateConst
import ru.stivosha.finalwork.data.consts.ViewType
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.helpers.DateHelper
import ru.stivosha.finalwork.ui.fragments.PostItemClickListener
import ru.stivosha.finalwork.ui.recycler.ItemTouchHelperAdapter
import java.text.SimpleDateFormat
import java.util.*

class PostListAdapter(
    private val onPostLikedListener: ((Post, Int) -> Unit)? = null,
    private val onPostDislikedListener: ((Post, Int) -> Unit)? = null,
    private val onPostDismissListener: ((Post, Int) -> Unit)? = null,
    private val postItemClickListener: PostItemClickListener? = null,
    private val dateHelper: DateHelper = DateHelper()
) : RecyclerView.Adapter<BaseViewHolder<Post>>(), ItemTouchHelperAdapter {
    private val nowDate = Date()

    var posts: MutableList<Post> = mutableListOf()

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts.toMutableList()
        calcDateDiff()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Post> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.IMAGE_POST -> PostViewHolder(inflater.inflate(R.layout.post_view_layout, parent, false))
            ViewType.ONLY_TEXT_POST -> PostOnlyTextViewHolder(inflater.inflate(R.layout.post_view_only_text_layout, parent, false))
            else -> PostViewHolder(inflater.inflate(R.layout.post_view_layout, parent, false))
        }
    }

    override fun getItemViewType(position: Int) = if (posts[position].imageContentUrl == null) ViewType.ONLY_TEXT_POST else ViewType.IMAGE_POST

    override fun onBindViewHolder(holder: BaseViewHolder<Post>, position: Int) {
        holder.bind(item = posts[position])
        holder.itemView.setOnClickListener {
            postItemClickListener?.onPostItemClick(holder.adapterPosition, posts[position], holder.myPostViewImageContent)
        }
    }

    override fun getItemCount() = posts.size

    override fun onItemDismiss(position: Int) {
        val post = posts[position]
        posts.removeAt(position)
        notifyItemRemoved(position)
        onPostDismissListener?.invoke(post, position)
    }

    override fun onItemLiked(position: Int) {
        if (posts[position].isLiked)
            onPostDislikedListener?.invoke(posts[position], position)
        else
            onPostLikedListener?.invoke(posts[position], position)
    }

    private fun calcDateDiff() {
        for (post in posts)
            post.dateDiff = dateHelper.calcDateDiff(post, nowDate)
    }
}

class PostViewHolder(override val containerView: View) : BaseViewHolder<Post>(containerView) {

    override fun bind(item: Post) {
        if (item.textContent.isNotEmpty())
            myPostViewTextContent.text = item.textContent
        else
            myPostViewTextContent.isGone = true
        myPostViewAuthor.text = item.author
        myPostViewDateCreation.text = item.dateDiff
        myPostViewLikesCount.text = item.likesCount.toString()
        myPostViewCommentsCount.text = item.commentsCount.toString()
        myPostViewShareCount.text = item.repostsCount.toString()
        Glide.with(containerView).load(item.authorImageUrl).into(myPostViewAvatar)
        Glide.with(containerView).load(item.imageContentUrl).into(myPostViewImageContent)
        myPostViewLikeButton.setImageResource(if (item.isLiked) R.drawable.ic_heart_active else R.drawable.ic_heart_not_active)
    }
}

class PostOnlyTextViewHolder(override val containerView: View) : BaseViewHolder<Post>(containerView) {

    override fun bind(item: Post) {
        myPostViewOnlyTextTextContent.text = item.textContent
        myPostViewOnlyTextAuthor.text = item.author
        myPostViewOnlyTextDateCreation.text = item.dateDiff
        myPostViewOnlyTextLikesCount.text = item.likesCount.toString()
        myPostViewOnlyTextCommentsCount.text = item.commentsCount.toString()
        myPostViewOnlyTextShareCount.text = item.repostsCount.toString()
        Glide.with(containerView).load(item.authorImageUrl).into(myPostViewOnlyTextAvatar)
        myPostViewOnlyTextLikeButton.setImageResource(if (item.isLiked) R.drawable.ic_heart_active else R.drawable.ic_heart_not_active)
    }
}

abstract class BaseViewHolder<in T>(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    abstract fun bind(item: T)
}