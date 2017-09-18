package com.oshurmamadov.domain.model

/**
 * Load video info domain model
 */
data class VideoInfoDomainModel(var empty: Boolean,
                                var videoLink: List<String?>,
                                var videoFormat: List<String?>,
                                var videoQuality: List<String?>)