package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoPropertiesDomainModel

/**
 * Load video duration repository
 */
interface LoadVideoPropertiesRepository {
    fun loadVideoDuration(videoUrl: String): VideoPropertiesDomainModel
}