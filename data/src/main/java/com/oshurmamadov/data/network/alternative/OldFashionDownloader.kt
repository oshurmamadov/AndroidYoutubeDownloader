package com.oshurmamadov.data.network.alternative

import android.util.Log
import com.oshurmamadov.data.common.HTTP_REQUEST_BYTES
import com.oshurmamadov.data.common.HTTP_REQUEST_HEAD
import com.oshurmamadov.data.common.HTTP_REQUEST_RANGE
import com.oshurmamadov.data.common.VIDEO_INFO_URL
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * Java downloading standard tools
 */
open class OldFashionDownloader {
    private val TAG = "OldFashionDownloader"
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

            Log.d(TAG, "VIDEO length : " + connection.contentLength)
            return connection.contentLength
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        return 0
    }

    open fun downloadVideoAndStoreIntoFile(videoUrl: String, videoDuration: String, videoSize: Int, trimmingBegin: String,
                                           trimmingEnd: String, file: File?): Boolean {
        var downloadStates = false
        var connection: HttpURLConnection? = null
        var inputStream: BufferedInputStream? = null
        var outputStream: FileOutputStream? = null

        // multiplication of trimming end(in milliseconds) and step(videoSize / videoDuration)
        val rangeEnding = trimmingEnd.toInt() * videoSize / videoDuration.toInt()

        try {
            connection = URL(videoUrl).openConnection() as HttpURLConnection
            //connection.setRequestProperty(HTTP_REQUEST_RANGE, HTTP_REQUEST_BYTES + trimmingBegin + "-" + rangeEnding)
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
            connection?.disconnect()
            inputStream?.close()
            outputStream?.close()
        }
        return downloadStates
    }

    open fun writeResponseBodyToDisk(videoUrl: String, gProperties: VideoPropertiesDomainModel,
                                     videoSize: Int, file: File?, trimmingBegin: String,
                                     trimmingEnd: String): Boolean {
        try {

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            var connection: HttpURLConnection? = null

            // multiplication of trimming end(in milliseconds) and step(videoSize / videoDuration)
            val step = videoSize / gProperties.duration.toInt()
            val rangeEnding = trimmingEnd.toInt() * step

            try {
                val fileReader = ByteArray(4096)

                var fileSizeDownloaded: Long = 0

                connection = URL(videoUrl).openConnection() as HttpURLConnection
                //connection.requestMethod = HTTP_REQUEST_HEAD
                //connection.setRequestProperty(HTTP_REQUEST_RANGE, "$HTTP_REQUEST_BYTES$trimmingBegin-$rangeEnding")
                connection.connect()


                inputStream = connection.inputStream
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d(TAG, "file download: $fileSizeDownloaded of $videoSize")
                }

                outputStream.flush()

                return true
            } catch (e: IOException) {
                return false
            } finally {
                connection?.disconnect()
                inputStream?.close()
                outputStream?.close()

            }
        } catch (e: IOException) {
            return false
        }

    }
}