package app.parviz.com.simpleyoutubedownloader.util
import kotlinx.coroutines.experimental.android.HandlerContext
/**
 * UI thread coroutine
 */
interface UIThreadCoroutine {
    fun getUICoroutine() : HandlerContext
}