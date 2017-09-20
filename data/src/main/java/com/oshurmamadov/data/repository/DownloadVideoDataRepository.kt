package com.oshurmamadov.data.repository

import com.oshurmamadov.domain.model.DownloadVideoDomainModel
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.DownloadVideoRepository

/**
 * Download video data repository
 */
class DownloadVideoDataRepository: DownloadVideoRepository {
    override fun downloadVideo(videoUrl: String, videoName: String, trimmingBegin: String, trimmingEnd: String, properties: VideoPropertiesDomainModel): DownloadVideoDomainModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}