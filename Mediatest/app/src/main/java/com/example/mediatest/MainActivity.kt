package com.example.mediatest

import android.annotation.SuppressLint
import android.media.session.PlaybackState
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.PlaybackStatsListener
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.example.mediatest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    private val playbackStateListener: Player.Listener = playbackStateListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23){
            initializePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if (Util.SDK_INT <23 || player == null){
            initializePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStop() {
        super.onStop()
        if(Util.SDK_INT >23){
            releasePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onPause() {
        super.onPause()
        if(Util.SDK_INT <= 23){
            releasePlayer()
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer(){
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                val mediaItem = MediaItem.Builder()
                    .setUri(getString(R.string.media_url_dash))
                    .setMimeType(MimeTypes.APPLICATION_MPD)
                    .build()
                exoPlayer.setMediaItem(mediaItem)

                val secondMediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
                exoPlayer.addMediaItem(secondMediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
    }

    //destroy player
    private fun releasePlayer(){
        player?.let {exoPlayer ->
            playWhenReady = exoPlayer.playWhenReady
            currentItem = exoPlayer.currentMediaItemIndex
            playbackPosition = exoPlayer.currentPosition
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    //to enable full screen experience
    @SuppressLint("InlinedApi")
    private fun hideSystemUI(){
       WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior=
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}

private const val TAG = "PlayerActivity"

private fun playbackStateListener() = object : Player.Listener{
    @OptIn(UnstableApi::class)
    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED"
            else -> "UNKNOWN STATE"
        }
        Log.d(TAG,"changed state to $stateString")
    }


    @OptIn(UnstableApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val playingString: String = if (isPlaying) "PLAYING" else "NOT PLAYING"
        Log.d(TAG,"Player is currently $playingString")
    }

    @OptIn(UnstableApi::class)
    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) ||
                events.contains(Player.EVENT_IS_PLAYING_CHANGED))
        {
            //update the ui
            Log.d(TAG, "Player needs to be updated")
        }
        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)){
            player.seekTo(5000L)
        }
    }
}