package app.parviz.com.simpleyoutubedownloader.common.extensions


import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Parviz_Oshurmamadov on 9/13/2017.
 */

fun AndroidSchedulers.getUIThreadScheduler() : Scheduler {
    return AndroidSchedulers.mainThread()
}