package com.example.a211124_chat_paging

object API {
    fun dateFormat(str_date: String): String? {
        var str_dateform = ""
        var ar_date = str_date.substring(0, 10).split("/")

        for (i in ar_date.indices) {
            when (i) {
                0 -> {
                    str_dateform += ar_date[0] + "년 "
                }
                1 -> {
                    str_dateform += ar_date[1] + "월 "
                }
                2 -> {
                    str_dateform += ar_date[2] + "일 "
                }
            }
        }
        return str_dateform
    }

}