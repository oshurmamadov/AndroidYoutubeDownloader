package app.parviz.com.simpleyoutubedownloader.di

import android.content.Context
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Parviz_Oshurmamadov on 9/6/2017.
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext() : Context {
        return context
    }

    @Provides
    @Singleton
    fun provideLoadVideoInfoPresenter() : LoadVideoInfoPresenter {
        return LoadVideoInfoPresenter()
    }
}