package ru.stivosha.finalwork.ui.recycler

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onItemLiked(position: Int)
}