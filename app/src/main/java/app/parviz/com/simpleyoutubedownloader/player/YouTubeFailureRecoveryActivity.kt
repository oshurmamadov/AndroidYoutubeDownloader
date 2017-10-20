package app.parviz.com.simpleyoutubedownloader.player

import android.content.Intent
import android.widget.Toast
import app.parviz.com.simpleyoutubedownloader.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

/**
 * An abstract activity which deals with recovering from errors which may occur during API
 * initialization, but can be corrected through user action.
 */
abstract class YouTubeFailureRecoveryActivity: YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val RECOVERY_DIALOG_REQUEST = 1

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        if (p1 != null) {
            if (p1.isUserRecoverableError) {
                p1.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val message = String.format(getString(R.string.error_player), p1.toString())
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
           getYouTubePlayer().initialize(getString(R.string.you_tube_key), this)
        }
    }

    protected abstract fun getYouTubePlayer(): YouTubePlayer.Provider
}