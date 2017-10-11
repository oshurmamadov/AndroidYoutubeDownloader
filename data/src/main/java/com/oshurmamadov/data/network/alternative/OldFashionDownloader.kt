package com.oshurmamadov.data.network.alternative

import com.oshurmamadov.data.common.HTTP_REQUEST_BYTES
import com.oshurmamadov.data.common.HTTP_REQUEST_HEAD
import com.oshurmamadov.data.common.HTTP_REQUEST_RANGE
import com.oshurmamadov.data.common.VIDEO_INFO_URL
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
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
            if (connection != null) connection.disconnect()
        }
        return 0
    }

    open fun downloadVideoAndStoreIntoDir(videoUrl: String, videoDuration: String, videoSize: Int, trimmingBegin: String,
                                          trimmingEnd: String, file: File?): Boolean {
        var downloadStates = false
        var connection: HttpURLConnection? = null
        var inputStream: BufferedInputStream? = null
        var outputStream: FileOutputStream? = null

        // multiplication of trimming end(in milliseconds) and step(videoSize / videoDuration)
        val rangeEnding = trimmingEnd.toInt() * videoSize / videoDuration.toInt()

        try {
            connection = URL(videoUrl).openConnection() as HttpURLConnection
            connection.setRequestProperty(HTTP_REQUEST_RANGE, HTTP_REQUEST_BYTES + trimmingBegin + "-" + rangeEnding)
            connection.connect()

            inputStream = BufferedInputStream(connection.inputStream)
            outputStream = FileOutputStream(file)

            //TODO determine whats is off: 0
            val data = ByteArray(videoSize)
            while (inputStream.read() != -1) {
                outputStream.write(inputStream.read())
            }

            System.out.println("video stream loaded and has been written to file")
            downloadStates = true
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            if (connection != null) connection.disconnect()
            if (inputStream != null) inputStream.close()
            if (outputStream != null) outputStream.close()
        }
        return downloadStates
    }
}