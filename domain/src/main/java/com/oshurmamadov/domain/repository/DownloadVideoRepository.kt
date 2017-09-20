package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.DownloadVideoDomainModel
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel

/**
 * Download video repository
 */
interface DownloadVideoRepository {
    fun downloadVideo(videoUrl: String,
                      videoName: String,
                      trimmingBegin: String,
                      trimmingEnd: String,
                      properties: VideoPropertiesDomainModel) : DownloadVideoDomainModel
}