package com.oshurmamadov.domain.interactor

import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import io.reactivex.Single

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class LoadVideoInfoInterActor(private val repository: LoadVideoInfoRepository,
                              threadScheduler: ThreadScheduler) : BaseInterActor<VideoInfoDomainModel>(threadScheduler) {
    private var videoInfoUrl = ""

    override fun setUrl(url: String) {
        videoInfoUrl = url
    }

    override fun buildInterActor(): Single<VideoInfoDomainModel> {
        if (videoInfoUrl.isEmpty())
            throw Exception("LoadVideoInfoInterActor : url is empty, really ? yo ?")

        return repository.loadVideoInfo(videoInfoUrl)
    }
}