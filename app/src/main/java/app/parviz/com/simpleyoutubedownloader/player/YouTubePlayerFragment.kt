package app.parviz.com.simpleyoutubedownloader.player


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.parviz.com.simpleyoutubedownloader.R


/**
 * A simple [Fragment] subclass.
 */
class YouTubePlayerFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_you_tube_player, container, false)
    }

}// Required empty public constructor
