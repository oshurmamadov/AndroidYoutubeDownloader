package app.parviz.com.simpleyoutubedownloader.player

/**
 * Custom youtube player listener
 */
interface CustomYoutubePlayerListener {

    /**
     * Play video stream
     */
    fun playVideo(url: String)

    /**
     * Stop video stream
     */
    fun stopVideo()
}