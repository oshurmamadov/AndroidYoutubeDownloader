package app.parviz.com.simpleyoutubedownloader.util
/**
 * UI thread coroutine
 */
interface UIThreadCoroutine {
    fun <T> runOnUICoroutine(runnable: suspend() -> T)
}