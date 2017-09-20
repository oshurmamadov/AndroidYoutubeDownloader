package com.oshurmamadov.domain.interactor

import android.util.Log
import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.LoadVideoPropertiesRepository

/**
 * Load video duration use case
 */
class LoadVideoPropertiesInterActor(private val repository: LoadVideoPropertiesRepository): BaseInterActor<VideoPropertiesDomainModel> {
    private var videoUrl = ""

    override fun setUrl(url: String) {
        videoUrl = url
    }

    override fun buildInterActor(): VideoPropertiesDomainModel {
        if (videoUrl.isEmpty())
            throw IllegalStateException("LoadVideoDuration: empty url")
        return repository.loadVideoDuration(videoUrl)
    }
}