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
//        val loadingStatus = OldFashionDownloader()
//                .downloadVideoAndStoreIntoFile(videoUrl, properties.duration, properties.size, trimmingBegin, trimmingEnd, videoFile)

        val loadingStatus = OldFashionDownloader().writeResponseBodyToDisk(
                videoUrl, properties, properties.size, videoFile, trimmingBegin, trimmingEnd)

        return if (loadingStatus) {
            ffMpeg.basicInit()
            val path = ffMpeg.trimVideo(
                    "00:00:04.00",//convertToAppropriateTimeFormat(trimmingBegin),
                    "00:00:08.00",//convertToAppropriateTimeFormat(trimmingEnd),
                    videoFile!!,
                    properties.format)

            //videoFile.deleteOnExit()

         //   DownloadVideoDomainModel(path)
            DownloadVideoDomainModel("")
        } else
            DownloadVideoDomainModel(EMPTY)
    }

    private fun convertToAppropriateTimeFormat(time: String): String {
        return time
    }
}