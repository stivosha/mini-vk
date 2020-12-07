package ru.stivosha.finalwork.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.stivosha.finalwork.data.entity.Post

class DiffCallback(
    private val oldList: List<Post>,
    private val newList: List<Post>
) : DiffUtil.Callback(){

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].textContent == newList[newItemPosition].textContent
}