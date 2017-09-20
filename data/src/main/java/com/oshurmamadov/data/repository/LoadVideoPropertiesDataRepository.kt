package com.oshurmamadov.data.repository

import android.media.MediaMetadataRetriever
import com.oshurmamadov.data.common.getVideoDuration
import com.oshurmamadov.data.network.alternative.OldFashionDownloader
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.LoadVideoPropertiesRepository

/**
 * Load video duration (in milliseconds) and size repository
 */
class LoadVideoPropertiesDataRepository : LoadVideoPropertiesRepository {
    override fun loadVideoDuration(videoUrl: String): VideoPropertiesDomainModel {
        return VideoPropertiesDomainModel(
                MediaMetadataRetriever().getVideoDuration(videoUrl),
                OldFashionDownloader().getVideoSize(videoUrl))
    }
}