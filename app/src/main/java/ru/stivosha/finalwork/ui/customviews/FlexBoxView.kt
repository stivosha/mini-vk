package ru.stivosha.finalwork.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import kotlin.math.max

class FlexBoxView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr){

    init{
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = paddingTop
        var currentRowWidth = paddingLeft
        var maxHeightRow = 0
        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, height)
            val childHeightWithMargin = child.measuredHeight + child.marginTop + child.marginBottom
            val childWidthWithMargin = child.measuredWidth + child.marginLeft + child.marginRight
            currentRowWidth += childWidthWithMargin
            if (currentRowWidth > desiredWidth - paddingRight){
                currentRowWidth = paddingLeft + childWidthWithMargin
                height += maxHeightRow
                maxHeightRow = childHeightWithMargin
            }else{
                maxHeightRow = max(maxHeightRow, childHeightWithMargin)
            }
        }
        setMeasuredDimension(desiredWidth, View.resolveSize(height + maxHeightRow + paddingBottom, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = t + paddingLeft
        var currentTop = l + paddingTop
        var maxHeightRow = 0
        children.forEach { child ->
            val childHeightWithMargin = child.measuredHeight + child.marginTop + child.marginBottom
            val childWidthWithMargin = child.measuredWidth + child.marginLeft + child.marginRight
            var currentRight = currentLeft + child.measuredWidth + child.marginLeft
            if (currentLeft + childWidthWithMargin > measuredWidth - paddingRight){
                currentLeft = t + paddingLeft + child.marginLeft
                currentRight = currentLeft + child.measuredWidth
                currentTop += maxHeightRow
                maxHeightRow = 0
            }else{
                currentLeft += child.marginLeft
            }
            child.layout(currentLeft, currentTop + child.marginTop,
                currentRight, currentTop + child.measuredHeight + child.marginTop)
            currentLeft += child.measuredWidth + child.marginRight
            maxHeightRow = max(maxHeightRow, childHeightWithMargin)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams?) = MarginLayoutParams(p)

    override fun checkLayoutParams(p: LayoutParams?) = p is MarginLayoutParams
}