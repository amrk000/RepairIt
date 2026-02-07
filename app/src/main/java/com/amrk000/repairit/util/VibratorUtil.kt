package com.amrk000.repairit.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibratorUtil {

    private fun getVibrator(context: Context): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun vibrate(context: Context, durationMs: Long) {
        val vibrator = getVibrator(context) ?: return

        val effect = VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }

    fun vibratePattern(context: Context, timings: LongArray, repeat: Int = -1) {
        val vibrator = getVibrator(context) ?: return

        val effect = VibrationEffect.createWaveform(timings, repeat)
        vibrator.vibrate(effect)
    }

    fun cancel(context: Context) {
        getVibrator(context)?.cancel()
    }

}