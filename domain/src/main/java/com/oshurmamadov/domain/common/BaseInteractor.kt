package com.oshurmamadov.domain.interactor.common

import com.oshurmamadov.domain.multithreading.JobExecutor
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
abstract class BaseInterActor<T>(private var threadScheduler: ThreadScheduler) {
    abstract fun setUrl(url: String)
    abstract fun buildInterActor() : Single<T>

    fun subscribe(observer: SingleObserver<T>) {
        buildInterActor()
                .subscribeOn(Schedulers.newThread())
                .observeOn(threadScheduler.getScheduler())
                .subscribe(observer)
    }
}