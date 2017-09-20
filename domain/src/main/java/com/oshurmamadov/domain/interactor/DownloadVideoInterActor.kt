package com.oshurmamadov.domain.interactor

import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.DownloadVideoDomainModel
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.DownloadVideoRepository

/**
 * Download video use case
 */
class DownloadVideoInterActor(private var repository: DownloadVideoRepository): BaseInterActor<DownloadVideoDomainModel> {
    private var videoName = ""
    private var trimmingEnd = ""
    private var trimmingBegin = ""
    private var videoSourceUrl = ""
    private var properties = VideoPropertiesDomainModel()

    fun setMainProperties(videoName: String, trimmingBegin: String, trimmingEnd: String, properties: VideoPropertiesDomainModel){
        this.videoName = videoName
        this.properties = properties
        this.trimmingEnd = trimmingEnd
        this.trimmingBegin = trimmingBegin
    }
    override fun setUrl(url: String) {
        videoSourceUrl = url
    }

    override fun buildInterActor(): DownloadVideoDomainModel {
        return repository.downloadVideo(isEmpty(videoSourceUrl), isEmpty(videoName), isEmpty(trimmingBegin), isEmpty(trimmingEnd), properties)
    }

    private fun isEmpty(value: String): String {
        if (value.isEmpty())
            throw IllegalStateException("DownloadVideoInterActor: empty essential argument")
        return value
    }
}