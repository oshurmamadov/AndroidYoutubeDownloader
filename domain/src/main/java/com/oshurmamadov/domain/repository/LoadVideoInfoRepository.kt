package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoInfoDomainModel
import io.reactivex.Single

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
interface LoadVideoInfoRepository {
    fun loadVideoInfo(url: String) : Single<VideoInfoDomainModel>
}