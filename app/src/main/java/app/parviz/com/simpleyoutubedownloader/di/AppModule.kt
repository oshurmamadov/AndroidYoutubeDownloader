package app.parviz.com.simpleyoutubedownloader.di

import android.content.Context
import app.parviz.com.simpleyoutubedownloader.util.UIThreadScheduler
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutine
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutineProvider
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.data.repository.LoadVideoInfoDataRepository
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.android.HandlerContext
import kotlinx.coroutines.experimental.android.UI
import javax.inject.Singleton

/**
 * Created by Parviz_Oshurmamadov on 9/6/2017.
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideThreadScheduler(): ThreadScheduler {
        return UIThreadScheduler()
    }

    @Provides
    @Singleton
    fun provideApiManager(): ApiManager {
        return ApiManager()
    }

    @Provides
    fun provideUIThreadCoroutine(): UIThreadCoroutine {
        return UIThreadCoroutineProvider()
    }

    @Provides
    fun provideLoadVideoInfoRepository(apiManager: ApiManager): LoadVideoInfoRepository {
        return LoadVideoInfoDataRepository(apiManager)
    }

    @Provides
    fun provideLoadVideoInfoInterActor(repository: LoadVideoInfoRepository): LoadVideoInfoInterActor {
        return LoadVideoInfoInterActor(repository)
    }

    @Provides
    @Singleton
    fun provideLoadVideoInfoPresenter(interActor: LoadVideoInfoInterActor, uiThreadCoroutine: UIThreadCoroutine): LoadVideoInfoPresenter {
        return LoadVideoInfoPresenter(interActor, uiThreadCoroutine)
    }

}