package app.parviz.com.simpleyoutubedownloader.di

import android.content.Context
import app.parviz.com.simpleyoutubedownloader.common.UIThreadScheduler
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.data.repository.LoadVideoInfoDataRepository
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
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
    fun provideThreadScheduler() : ThreadScheduler {
        return UIThreadScheduler()
    }

    @Provides
    @Singleton
    fun provideApiManager() : ApiManager {
        return ApiManager()
    }

    @Provides
    fun provideLoadVideoInfoRepository(apiManager: ApiManager) : LoadVideoInfoRepository {
        return LoadVideoInfoDataRepository(apiManager)
    }

    @Provides
    fun provideLoadVideoInfoInterActor(repository: LoadVideoInfoRepository, threadScheduler: ThreadScheduler ) : LoadVideoInfoInterActor {
        return LoadVideoInfoInterActor(repository, threadScheduler)
    }

    @Provides
    @Singleton
    fun provideLoadVideoInfoPresenter(interActor: LoadVideoInfoInterActor) : LoadVideoInfoPresenter {
        return LoadVideoInfoPresenter(interActor)
    }
}