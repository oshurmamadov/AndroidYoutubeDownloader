package com.oshurmamadov.data.common

import okhttp3.ResponseBody

/**
 * Created by Parviz_Oshurmamadov on 9/13/2017.
 */

fun ResponseBody.convertBodyToString() : String {
    return this.byteStream().bufferedReader().use { it.readText()}
}