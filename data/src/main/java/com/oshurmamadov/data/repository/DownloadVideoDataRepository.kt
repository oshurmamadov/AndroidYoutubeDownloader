package com.oshurmamadov.data.repository

import com.oshurmamadov.data.common.EMPTY
import com.oshurmamadov.data.common.basicInit
import com.oshurmamadov.data.common.getOutputMediaFile
import com.oshurmamadov.data.common.trimVideo
import com.oshurmamadov.data.network.alternative.OldFashionDownloader
import com.oshurmamadov.domain.model.DownloadVideoDomainModel
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.DownloadVideoRepository
import com.oshurmamadov.domain.util.OSEnvironment
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Download video data repository
 */
class DownloadVideoDataRepository(private var osEnvironment: OSEnvironment, var ffMpeg: FFmpeg): DownloadVideoRepository {
    override fun downloadVideo(videoUrl: String,
                               videoName: String,
                               trimmingBegin: String,
                               trimmingEnd: String,
                               properties: VideoPropertiesDomainModel): DownloadVideoDomainModel {

        val videoFile = File(videoName).getOutputMediaFile(osEnvironment, properties.format)

        val loadingStatus = OldFashionDownloader().writeResponseBodyToDisk(
                videoUrl, properties, properties.size, videoFile, trimmingBegin, trimmingEnd)

        return if (loadingStatus) {
            ffMpeg.basicInit()
            val path = ffMpeg.trimVideo(
                    convertToAppropriateTimeFormat(trimmingBegin.toLong()),
                    convertToAppropriateTimeFormat(trimmingEnd.toLong()),
                    videoFile!!,
                    properties.format)

            videoFile.deleteOnExit()

            DownloadVideoDomainModel(path)
        } else
            DownloadVideoDomainModel(EMPTY)
    }

    private fun convertToAppropriateTimeFormat(millis: Long): String {
        val duration = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
        System.out.println("DownloadVideoDataRepository  duration to cut: $duration")
        return duration
    }
}