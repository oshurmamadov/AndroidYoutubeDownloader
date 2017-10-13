package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.responsehandler.ResponseHandler

/**
 * Load video duration repository
 */
interface LoadVideoPropertiesRepository {
    fun loadVideoDuration(videoUrl: String): ResponseHandler<VideoPropertiesDomainModel>
}