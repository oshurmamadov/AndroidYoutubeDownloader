package app.parviz.com.simpleyoutubedownloader.player

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.parviz.com.simpleyoutubedownloader.LoadActivity
import app.parviz.com.simpleyoutubedownloader.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import java.util.*

/**
 * YouTube player fragment
 */
class CustomYouTubePlayerFragment : Fragment() {

    private var player: YouTubePlayer? = null

    override  fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_you_tube_player, container, false)

        initPlayerContainer()

        return view
    }

    private fun initPlayerContainer() {
        val playerFragment = YouTubePlayerSupportFragment.newInstance()
        childFragmentManager.beginTransaction().add(R.id.youtube_fragment, playerFragment).commit()

        playerFragment.initialize(getString(R.string.you_tube_key), OnInitializedListenerImpl())
        (activity as LoadActivity).setCustomYoutubePlayerListener(CustomYoutubePlayerListenerImpl())
    }

    private fun playVideoStream(videoUrl: String) {
        checkNotNull(player)
        player!!.cueVideo(videoUrl)
        player!!.play()
    }

    inner class OnInitializedListenerImpl : OnInitializedListener {
        override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, mPlayer: YouTubePlayer, wasRestored: Boolean) {
            if (!wasRestored) {
                player = mPlayer
            }
        }

        override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
            Toast.makeText(context, "Failed. SGEYN", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CustomYoutubePlayerListenerImpl: CustomYoutubePlayerListener {
        override fun playVideo(url: String) {
            playVideoStream(url)
        }

        override fun stopVideo() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
