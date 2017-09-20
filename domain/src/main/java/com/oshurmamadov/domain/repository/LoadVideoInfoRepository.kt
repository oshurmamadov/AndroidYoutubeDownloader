package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoInfoDomainModel
import io.reactivex.Single

/**
 * Domain layer load video info repository
 */
interface LoadVideoInfoRepository {
    fun loadVideoInfo(url: String) : VideoInfoDomainModel
}