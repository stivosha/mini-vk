package ru.stivosha.finalwork.extentions

import android.content.Context

fun Context.dpToPx(dp: Int) = dp.toFloat() * this.resources.displayMetrics.density