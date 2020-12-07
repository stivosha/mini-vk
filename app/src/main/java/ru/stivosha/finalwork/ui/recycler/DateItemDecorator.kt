package ru.stivosha.finalwork.ui.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import ru.stivosha.finalwork.R
import ru.stivosha.finalwork.data.entity.Post
import ru.stivosha.finalwork.ui.recycler.adapters.PostListAdapter


class DateItemDecorator(parent: RecyclerView, @LayoutRes resId: Int) :
    RecyclerView.ItemDecoration() {

    private val layout: View = LayoutInflater.from(parent.context).inflate(resId, parent, false)

    init {
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val lp = layout.layoutParams as ViewGroup.MarginLayoutParams
        layout.layout(parent.left, 0, parent.right - lp.rightMargin - lp.leftMargin,
            layout.measuredHeight)
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val posts = (parent.adapter as PostListAdapter).posts
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0 && isItemNeedDateDraw(posts, position)) {
                c.save()
                layout.findViewById<TextView>(R.id.dateDecoratorText).text = posts[position].dateDiff
                layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                layout.layout(parent.left, 0, parent.right - lp.rightMargin - lp.leftMargin,
                    layout.measuredHeight)
                val height = layout.measuredHeight
                val top = view.top - height - lp.bottomMargin
                c.translate(lp.leftMargin.toFloat(), top.toFloat())
                layout.draw(c)
                c.restore()
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val posts = (parent.adapter as PostListAdapter).posts
        val position = parent.getChildAdapterPosition(view)
        if (position >= 0 && isItemNeedDateDraw(posts, position)) {
            val lp = layout.layoutParams as ViewGroup.MarginLayoutParams
            outRect.set(0, layout.measuredHeight + lp.bottomMargin + lp.topMargin, 0, 0)
        } else {
            outRect.setEmpty()
        }
    }

    private fun isItemNeedDateDraw(posts: List<Post>, position: Int) = position == 0
            || position < posts.size
            && posts[position - 1].dateDiff != posts[position].dateDiff
}