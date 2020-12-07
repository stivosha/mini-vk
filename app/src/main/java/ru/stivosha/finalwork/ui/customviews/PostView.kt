package ru.stivosha.finalwork.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import kotlinx.android.synthetic.main.post_view.view.*
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.extentions.dpToPx

class PostView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr){

    init {
        LayoutInflater.from(context).inflate(R.layout.post_view, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        var width = 0

        val sizeMeasureSpecAvatar = MeasureSpec.makeMeasureSpec(context.dpToPx(72).toInt(), MeasureSpec.EXACTLY)
        measureChildWithMargins(postViewAvatar, sizeMeasureSpecAvatar, width, sizeMeasureSpecAvatar, height)
        width += postViewAvatar.measuredWidth
        measureChildWithMargins(postViewTittle, widthMeasureSpec, width, heightMeasureSpec, height)
        height += postViewTittle.measuredHeight
        measureChildWithMargins(
            postViewDateCreation,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        width = 0
        height = postViewAvatar.measuredHeight
        measureChildWithMargins(
            postViewTextContent,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        height += postViewTextContent.measuredHeight
        val limitHeightMeasureSpec = MeasureSpec.makeMeasureSpec(context.dpToPx(550).toInt(), MeasureSpec.AT_MOST)
        measureChildWithMargins(
            postViewImageContent,
            widthMeasureSpec,
            width,
            limitHeightMeasureSpec,
            height
        )
        height = postViewImageContent.measuredHeight + postViewTextContent.measuredHeight - context.dpToPx(10).toInt()
        measureChildWithMargins(
            postViewLikeButton,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        measureChildWithMargins(
            postViewLikesCount,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        width += postViewLikesCount.measuredWidth
        measureChildWithMargins(
            postViewCommentButton,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        width += postViewCommentButton.measuredWidth
        measureChildWithMargins(
            postViewCommentsCount,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        width += postViewCommentsCount.measuredWidth
        measureChildWithMargins(
            postViewShareButton,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        width += postViewShareButton.measuredWidth
        measureChildWithMargins(
            postViewShareCount,
            widthMeasureSpec,
            width,
            heightMeasureSpec,
            height
        )
        height += postViewShareButton.measuredHeight
        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = l + paddingLeft
        var currentTop = t + paddingTop

        postViewAvatar.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewAvatar.measuredWidth,
            currentTop + postViewAvatar.measuredHeight
        )
        currentLeft += currentLeft + postViewAvatar.measuredWidth

        postViewTittle.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewTittle.measuredWidth,
            currentTop + postViewTittle.measuredHeight
        )
        currentTop += postViewTittle.measuredHeight

        postViewDateCreation.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewDateCreation.measuredWidth,
            currentTop + postViewDateCreation.measuredHeight
        )
        currentTop = t + paddingTop + postViewAvatar.measuredHeight
        currentLeft = l + paddingLeft

        postViewTextContent.layout(
            currentLeft,
            currentTop,
            measuredWidth,
            currentTop + postViewTextContent.measuredHeight
        )
        currentTop += postViewTextContent.measuredHeight
        postViewImageContent.layout(
            currentLeft,
            currentTop,
            measuredWidth - currentLeft,
            postViewImageContent.measuredHeight
        )
        currentTop = postViewImageContent.measuredHeight + context.dpToPx(10).toInt()
        currentLeft = l + paddingLeft * 2
        postViewLikeButton.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewLikeButton.measuredWidth,
            currentTop + postViewLikeButton.measuredHeight
        )
        currentLeft += postViewLikeButton.measuredWidth + context.dpToPx(5).toInt()
        postViewLikesCount.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewLikesCount.measuredWidth,
            currentTop + postViewLikesCount.measuredHeight
        )
        currentLeft = measuredWidth / 2 - postViewCommentButton.measuredWidth
        postViewCommentButton.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewCommentButton.measuredWidth,
            currentTop + postViewCommentButton.measuredHeight
        )
        currentLeft += postViewCommentButton.measuredWidth + context.dpToPx(5).toInt()
        postViewCommentsCount.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewCommentsCount.measuredWidth,
            currentTop + postViewCommentsCount.measuredHeight
        )

        currentLeft = r - postViewShareButton.measuredWidth * 2 - paddingEnd * 2 - context.dpToPx(5).toInt()
        postViewShareButton.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewShareButton.measuredWidth,
            currentTop + postViewShareButton.measuredHeight
        )
        currentLeft += postViewShareButton.measuredWidth + context.dpToPx(5).toInt()
        postViewShareCount.layout(
            currentLeft,
            currentTop,
            currentLeft + postViewShareCount.measuredWidth,
            currentTop + postViewShareCount.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(
        WRAP_CONTENT,
        WRAP_CONTENT
    )

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}