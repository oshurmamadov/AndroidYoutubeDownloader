package app.parviz.com.simpleyoutubedownloader.common

import com.oshurmamadov.domain.multithreading.ThreadScheduler
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Parviz_Oshurmamadov on 9/13/2017.
 */
class UIThreadScheduler : ThreadScheduler {

    override fun getScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}