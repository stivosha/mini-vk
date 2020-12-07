package ru.stivosha.finalwork.ui.recycler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.post_view_layout.*
import kotlinx.android.synthetic.main.post_view_layout.view.*
import kotlinx.android.synthetic.main.profile_fragment_profile_element.*
import kotlinx.android.synthetic.main.profile_fragment_profile_element.view.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.consts.ViewType
import ru.stivosha.finalwork.data.entity.*
import ru.stivosha.finalwork.helpers.DateHelper
import java.util.*

class ProfileAdapter(
    private val onNewPostButtonClickedListener: ((ProfileDetailObject, Int) -> Unit),
    private val onPostClickedListener: ((ProfileDetailObject, Int) -> Unit),
    private val onSeeMoreButtonClickedListener: (() -> Unit),
    private val onAttachPhotoButtonClickedListener: (() -> Unit),
    private val onEditProfileButtonClickedListener: (() -> Unit)
) : RecyclerView.Adapter<BaseViewHolder<ProfileDetailObject>>() {
    private val differ: AsyncListDiffer<ProfileDetailObject> = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<ProfileDetailObject>) {
        differ.submitList(list)
    }

    fun getProfile(): ProfileDetailObject {
        return differ.currentList[0]
    }

    override fun getItemViewType(position: Int) = if (position == 0) ViewType.PROFILE_ELEMENT else ViewType.POST_ELEMENT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProfileDetailObject> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.PROFILE_ELEMENT -> ProfileViewHolder(inflater.inflate(R.layout.profile_fragment_profile_element, parent, false))
            ViewType.POST_ELEMENT -> ProfilePostViewHolder(inflater.inflate(R.layout.post_view_layout, parent, false))
            else -> ProfilePostViewHolder(inflater.inflate(R.layout.post_view_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ProfileDetailObject>, position: Int) {
        if (holder is ProfileViewHolder) {
            holder.bind(differ.currentList[position])
            holder.itemView.profileFragmentNewPostBlockTextView.setOnClickListener {
                onNewPostButtonClickedListener.invoke(differ.currentList[position], position)
            }
            holder.itemView.profileFragmentNewPostBlockAttach.setOnClickListener {
                onAttachPhotoButtonClickedListener.invoke()
            }
            holder.itemView.profileFragmentSeeMoreButton.setOnClickListener {
                onSeeMoreButtonClickedListener.invoke()
            }
            holder.itemView.profileFragmentEditButton.setOnClickListener{
                onEditProfileButtonClickedListener.invoke()
            }
        }
        if (holder is ProfilePostViewHolder) {
            holder.bind(differ.currentList[position])
            holder.itemView.setOnClickListener {
                onPostClickedListener.invoke(differ.currentList[position], position)
            }
            holder.itemView.postCommentsBlock.setOnClickListener {
                onPostClickedListener.invoke(differ.currentList[position], position)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfileDetailObject>() {
            override fun areItemsTheSame(oldItem: ProfileDetailObject, newItem: ProfileDetailObject): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ProfileDetailObject, newItem: ProfileDetailObject): Boolean {
                if (oldItem is User && newItem is User)
                    return oldItem.id == newItem.id
                if (oldItem is Post && newItem is Post)
                    return oldItem.textContent == newItem.textContent && oldItem.imageContentUrl == newItem.imageContentUrl
                return false
            }
        }
    }
}

class ProfileViewHolder(override val containerView: View) : BaseViewHolder<ProfileDetailObject>(containerView) {
    override fun bind(item: ProfileDetailObject) {
        val user = item as User
        Glide.with(containerView).load(user.photo_url).into(profileFragmentAvatar)
        Glide.with(containerView).load(user.photo_url).into(profileFragmentNewPostBlockAvatar)
        profileFragmentName.text = user.first_last_name
        if (user.status != null) {
            profileFragmentStatus.isVisible = true
            profileFragmentStatus.text = user.status
        }
        if (user.educationPlaceName != null) {
            profileFragmentEducationPlaceBlock.isVisible = true
            profileFragmentEducationPlaceText.text = profileFragmentEducationPlaceText.text.toString().format(user.educationPlaceName)
        }
        if (user.cityName != null) {
            profileFragmentCityBlock.isVisible = true
            profileFragmentCityText.text = profileFragmentCityText.text.toString().format(user.cityName)
        }
        profileFragmentFollowersBlock.isVisible = true
        profileFragmentSeeMoreButton.isVisible = true
        profileFragmentFollowersText.text = profileFragmentFollowersText.text.toString().format(user.followersCount)
    }
}

class ProfilePostViewHolder(override val containerView: View) : BaseViewHolder<ProfileDetailObject>(containerView) {

    override fun bind(item: ProfileDetailObject) {
        val itemPost = item as Post
        if (itemPost.textContent.isNotEmpty())
            myPostViewTextContent.text = itemPost.textContent
        else
            myPostViewTextContent.isGone = true
        myPostViewAuthor.text = itemPost.author
        myPostViewDateCreation.text = DateHelper.timeToString(itemPost.dateCreation, Date())
        myPostViewLikesCount.text = itemPost.likesCount.toString()
        myPostViewCommentsCount.text = itemPost.commentsCount.toString()
        myPostViewShareCount.text = itemPost.repostsCount.toString()
        Glide.with(containerView).load(itemPost.authorImageUrl).into(myPostViewAvatar)
        Glide.with(containerView).load(itemPost.imageContentUrl).into(myPostViewImageContent)
        myPostViewLikeButton.setImageResource(if (itemPost.isLiked) R.drawable.ic_heart_active else R.drawable.ic_heart_not_active)
    }
}