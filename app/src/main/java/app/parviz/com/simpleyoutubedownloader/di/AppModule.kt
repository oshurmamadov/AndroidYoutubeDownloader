package app.parviz.com.simpleyoutubedownloader.di

import android.content.Context
import android.media.MediaMetadataRetriever
import app.parviz.com.simpleyoutubedownloader.downloadvideo.presenter.DownloadVideoPresenter
import app.parviz.com.simpleyoutubedownloader.util.UIThreadScheduler
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.util.OSEnvironmentProvider
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutine
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutineProvider
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.data.repository.DownloadVideoDataRepository
import com.oshurmamadov.data.repository.LoadVideoPropertiesDataRepository
import com.oshurmamadov.data.repository.LoadVideoInfoDataRepository
import com.oshurmamadov.domain.interactor.DownloadVideoInterActor
import com.oshurmamadov.domain.interactor.LoadVideoPropertiesInterActor
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.multithreading.ThreadScheduler
import com.oshurmamadov.domain.repository.DownloadVideoRepository
import com.oshurmamadov.domain.repository.LoadVideoPropertiesRepository
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import com.oshurmamadov.domain.util.OSEnvironment
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

/**
 * Main Dagger App Module
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
    fun provideOSEnvironment(): OSEnvironment {
        return OSEnvironmentProvider()
    }

    @Provides
    fun getMediaMetadataRetriever(): MediaMetadataRetriever {
        return MediaMetadataRetriever()
    }

    @Provides
    fun getFFMPEG(context: Context): FFmpeg {
        return FFmpeg.getInstance(context)
    }

    //------------------------------ BEGIN of Repositories
    @Provides
    fun provideLoadVideoInfoRepository(apiManager: ApiManager): LoadVideoInfoRepository {
        return LoadVideoInfoDataRepository(apiManager)
    }

    @Provides
    fun provideLoadVideoDurationRepository(mediaMetadataRetriever: MediaMetadataRetriever): LoadVideoPropertiesRepository {
        return LoadVideoPropertiesDataRepository(mediaMetadataRetriever)
    }

    @Provides
    fun provideDownloadVideoRepository(osEnvironment: OSEnvironment, ffMpeg: FFmpeg): DownloadVideoRepository {
        return DownloadVideoDataRepository(osEnvironment, ffMpeg)
    }

    //------------------------------ BEGIN of InterActors aka UseCase
    @Provides
    fun provideLoadVideoInfoInterActor(repository: LoadVideoInfoRepository): LoadVideoInfoInterActor {
        return LoadVideoInfoInterActor(repository)
    }

    @Provides
    fun provideLoadVideoPropertiesInterActor(repository: LoadVideoPropertiesRepository): LoadVideoPropertiesInterActor {
        return LoadVideoPropertiesInterActor(repository)
    }

    @Provides
    fun provideDownloadVideoInterActor(repository: DownloadVideoRepository): DownloadVideoInterActor {
        return DownloadVideoInterActor(repository)
    }

    //------------------------------ BEGIN of Presenters
    @Provides
    @Singleton
    fun provideLoadVideoInfoPresenter(infoInterActor: LoadVideoInfoInterActor,
                                      uiThreadCoroutine: UIThreadCoroutine): LoadVideoInfoPresenter {
        return LoadVideoInfoPresenter(infoInterActor, uiThreadCoroutine)
    }

    @Provides
    @Singleton
    fun provideDownloadVideoPresenter(propertiesInterActor: LoadVideoPropertiesInterActor,
                                      downloadVideoInterActor: DownloadVideoInterActor,
                                      uiThreadCoroutine: UIThreadCoroutine): DownloadVideoPresenter {
        return DownloadVideoPresenter(propertiesInterActor, downloadVideoInterActor, uiThreadCoroutine)
    }
}