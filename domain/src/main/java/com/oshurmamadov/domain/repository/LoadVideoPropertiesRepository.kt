package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.responsehandler.ResponseHandler

/**
 * Load video duration repository
 */
interface LoadVideoPropertiesRepository {
    fun loadVideoProperties(videoUrl: String): ResponseHandler<VideoPropertiesDomainModel>
}