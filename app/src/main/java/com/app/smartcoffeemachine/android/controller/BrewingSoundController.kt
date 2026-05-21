package com.app.smartcoffeemachine.android.controller

import android.content.Context
import android.media.MediaPlayer
import com.app.smartcoffeemachine.R

class BrewingSoundController(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play() {
        if (mediaPlayer?.isPlaying == true) return

        mediaPlayer = MediaPlayer.create(context, R.raw.coffee_machine).apply {
            isLooping = true
            start()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}