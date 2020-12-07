package ru.stivosha.finalwork.data.consts

import android.graphics.Bitmap
import ru.stivosha.finalwork.data.entity.Post
import java.text.SimpleDateFormat

object PostList {
    fun create(): MutableList<Post>{
        val data = mutableListOf<Post>()
        data.add(
            Post(
                1,
                "АМДЭВС",
                "text1",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("05-10-2020"),
                authorImageUrl = "https://sun3-12.userapi.com/impf/c637622/v637622257/5b5bf/xjT2gn-8MU4.jpg?size=100x0&quality=88&crop=0,0,2159,2159&sign=7fe30e43db5c497f94b4668e03de77fc&ava=1",
                imageContentUrl = "https://pp.userapi.com/c627731/v627731855/16638/-djrEjLZQSU.jpg"
            )
        )
        data.add(
            Post(
                2,
                "АМДЭВС",
                "text2",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("05-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        data.add(
            Post(
                3,
                "АМДЭВС",
                "text3",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("05-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        data.add(
            Post(
                4,
                "АМДЭВС",
                "text4",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("05-10-2020"),
                authorImageUrl = "https://sun3-12.userapi.com/impf/c637622/v637622257/5b5bf/xjT2gn-8MU4.jpg?size=100x0&quality=88&crop=0,0,2159,2159&sign=7fe30e43db5c497f94b4668e03de77fc&ava=1",
                imageContentUrl = "https://pp.userapi.com/c627731/v627731855/16638/-djrEjLZQSU.jpg"
            )
        )
        data.add(
            Post(
                5,
                "АМДЭВС",
                "text5",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("04-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        data.add(
            Post(
                6,
                "АМДЭВС",
                "text6",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("03-10-2020"),
                authorImageUrl = "https://sun3-12.userapi.com/impf/c637622/v637622257/5b5bf/xjT2gn-8MU4.jpg?size=100x0&quality=88&crop=0,0,2159,2159&sign=7fe30e43db5c497f94b4668e03de77fc&ava=1",
                imageContentUrl = "https://pp.userapi.com/c627731/v627731855/16638/-djrEjLZQSU.jpg"
            )
        )
        data.add(
            Post(
                7,
                "АМДЭВС",
                "text7",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("02-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        data.add(
            Post(
                8,
                "АМДЭВС",
                "text8",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("01-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        data.add(
            Post(
                9,
                "АМДЭВС",
                "text9",
                SimpleDateFormat(DateConst.DATE_FORMAT).parse("01-10-2020"),
                authorImageUrl = "https://sun3-13.userapi.com/impg/c857332/v857332190/660ab/9Yt-kEB5PW4.jpg?size=100x0&quality=88&crop=11,10,507,507&sign=41a2505de301a330c1f95d89e2b68859&ava=1"
            )
        )
        return data
    }

    private fun createEmptyBitmap() : Bitmap{
        val w = 100
        val h = 100
        val conf = Bitmap.Config.ARGB_8888
        return Bitmap.createBitmap(w, h, conf)
    }
}