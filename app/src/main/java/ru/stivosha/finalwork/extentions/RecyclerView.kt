package ru.stivosha.finalwork.extentions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.getCurrentPosition() = (this.layoutManager as LinearLayoutManager?)?.findFirstVisibleItemPosition() ?: -1