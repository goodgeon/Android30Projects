package com.example.recorder

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context: Context,
    attrs: AttributeSet
): AppCompatTextView(context, attrs) {
    private var StartTimeStamp: Long = 0L

    private val countupAction: Runnable = object: Runnable {
        override fun run() {
            var currentTimeStamp = SystemClock.elapsedRealtime()
            val countTimeSeconds = ((currentTimeStamp - StartTimeStamp)/1000L).toInt()

            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp() {
        StartTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countupAction)
    }

    fun stopCountUp() {
        handler?.removeCallbacks(countupAction)
    }

    fun clearCountTime() {
        updateCountTime(0)
    }

    fun updateCountTime(countTimeSeconds: Int) {
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }
}