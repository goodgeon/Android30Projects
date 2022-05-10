package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekBar)
    }

    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }

    private var currentTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    currentTimer?.cancel()
                    currentTimer = null
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    currentTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
                    currentTimer?.start()
                }
            }
        )
    }

    private fun createCountDownTimer(initialMillis : Long): CountDownTimer {
        return object :CountDownTimer(initialMillis,1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                //todo: update TextView, SeekBar to 00
                updateRemainTime(0)
                updateSeekBar(0)
            }
        }
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }
}