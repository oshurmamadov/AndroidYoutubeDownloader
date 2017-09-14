package com.oshurmamadov.data.network.alternative

import android.util.Log
import com.oshurmamadov.data.common.VIDEO_INFO_URL
import java.io.BufferedInputStream
import java.net.URL

/**
 * Created by Parviz_Oshurmamadov on 9/14/2017.
 */
open class OldFashionDownloader {

    open fun downloadStreamAndComeWithBuilder(key : String) : StringBuilder {
        val builder = StringBuilder()
        try {
            System.setProperty("http.keepAlive", "false")

            val bufferedInputStream = BufferedInputStream(URL(VIDEO_INFO_URL + key).openStream())

            while (bufferedInputStream.read() != -1) {
                builder.append(bufferedInputStream.read().toChar())
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }
        return builder
    }
}