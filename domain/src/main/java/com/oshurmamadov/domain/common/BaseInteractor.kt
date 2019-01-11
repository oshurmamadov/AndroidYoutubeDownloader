package com.oshurmamadov.domain.interactor.common

import com.oshurmamadov.domain.multithreading.JobExecutor
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
interface BaseInterActor<out T> {
    fun setUrl(url: String)
    suspend fun buildInterActor() : T
}