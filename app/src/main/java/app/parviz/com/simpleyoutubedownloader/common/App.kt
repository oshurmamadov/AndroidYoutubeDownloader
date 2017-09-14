package app.parviz.com.simpleyoutubedownloader.common

import android.app.Application
import app.parviz.com.simpleyoutubedownloader.di.AppComponent
import app.parviz.com.simpleyoutubedownloader.di.AppModule
import app.parviz.com.simpleyoutubedownloader.di.DaggerAppComponent

/**
 * Created by Parviz_Oshurmamadov on 9/5/2017.
 */

class App: Application() {

    companion object {
        @JvmStatic lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
        graph = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}