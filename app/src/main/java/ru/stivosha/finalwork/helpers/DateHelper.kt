package ru.stivosha.finalwork.helpers

import ru.stivosha.finalwork.data.consts.DateConst
import ru.stivosha.finalwork.data.consts.StringConst
import ru.stivosha.finalwork.data.entity.Post
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    fun calcDateDiff(post: Post, nowDate: Date): String =
        when (nowDate.time - post.dateCreation.time) {
            in -DateConst.MILLISECONDS_ONE_DAY..DateConst.MILLISECONDS_ONE_DAY -> StringConst.TODAY
            in DateConst.MILLISECONDS_ONE_DAY..DateConst.MILLISECONDS_TWO_DAY -> StringConst.YESTERDAY
            else -> dateFormat.format(post.dateCreation)
        }

    companion object {
        private val dateFormat = SimpleDateFormat(DateConst.DATE_FORMAT_FOR_LIST)
        private val dateFormatRecent = SimpleDateFormat(DateConst.DATE_FORMAT_FOR_RECENT)

        fun timeToString(elementDate: Date, nowDate: Date): String {
            return when (nowDate.time - elementDate.time) {
                in -DateConst.MILLISECONDS_TEN_SECONDS..DateConst.MILLISECONDS_TEN_SECONDS -> StringConst.JUST_NOW
                in DateConst.MILLISECONDS_TEN_SECONDS..DateConst.MILLISECONDS_ONE_DAY -> "${StringConst.TODAY} at ${dateFormatRecent.format(elementDate)}"
                in DateConst.MILLISECONDS_ONE_DAY..DateConst.MILLISECONDS_TWO_DAY -> "${StringConst.YESTERDAY} at ${dateFormatRecent.format(elementDate)}"
                else -> dateFormat.format(elementDate)
            }
        }
    }
}