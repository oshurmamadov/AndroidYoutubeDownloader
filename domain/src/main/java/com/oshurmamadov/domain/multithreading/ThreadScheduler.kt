package com.oshurmamadov.domain.multithreading

import io.reactivex.Scheduler

/**
 * Created by Parviz_Oshurmamadov on 9/13/2017.
 */
interface ThreadScheduler {
    fun getScheduler() : Scheduler
}