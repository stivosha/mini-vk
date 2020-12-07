package ru.stivosha.finalwork.ui.recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PostItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START)
            adapter.onItemDismiss(viewHolder.adapterPosition)
        else
            adapter.onItemLiked(viewHolder.adapterPosition)
    }
}