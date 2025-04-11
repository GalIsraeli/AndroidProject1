package com.gamepackage.androidproject1.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log


fun Context.vibrateDevice(durationMillis: Long = 500) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // API 31 and above: VibratorManager
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            // API 26 to 30: Vibrator with VibrationEffect
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
        }

        else -> {
            // Below API 26: Deprecated vibrate method without VibrationEffect
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMillis)
        }
    }
}

fun Context.playSoundEffect(res: Int) {
    var mediaPlayer: MediaPlayer? = null

    try {
        mediaPlayer = MediaPlayer.create(this, res)

        if (mediaPlayer == null) {
            Log.e("SoundEffect", "MediaPlayer creation failed: Check audio file.")
            return
        }

        mediaPlayer.setOnCompletionListener {
            it.release()
        }

        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("SoundEffect", "MediaPlayer error occurred. What: $what Extra: $extra")
            mp.release()
            true
        }

        mediaPlayer.start()

    } catch (ex: Exception) {
        Log.e("SoundEffect", "Exception playing sound effect", ex)
        mediaPlayer?.release()
    }
}