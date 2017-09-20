package com.oshurmamadov.data.network.alternative

import com.oshurmamadov.data.common.VIDEO_INFO_URL
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Java downloading standard tools
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
        //str = bufferedInputStream.bufferedReader().use { it.readText() }
    }

    open fun getVideoSize(url: String): Int {
        var connection: HttpURLConnection? = null
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            return connection.contentLength
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (connection != null)
                connection.disconnect()
        }
        return 0
    }
}