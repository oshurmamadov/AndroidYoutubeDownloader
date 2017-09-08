package app.parviz.com.simpleyoutubedownloader.di

import app.parviz.com.simpleyoutubedownloader.LoadActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Parviz_Oshurmamadov on 9/6/2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(loadActivity: LoadActivity)
}