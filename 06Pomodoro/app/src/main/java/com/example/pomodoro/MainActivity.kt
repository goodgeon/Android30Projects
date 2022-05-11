package com.example.pomodoro

import android.media.SoundPool
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

    private val soundpool = SoundPool.Builder().build()
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        initSounds()
    }

    override fun onPause() {
        super.onPause()
        soundpool.autoPause()
    }

    override fun onResume() {
        super.onResume()
        soundpool.autoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundpool.release()
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
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    if(seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }
        )
    }

    private fun initSounds() {
        tickingSoundId = soundpool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundpool.load(this, R.raw.timer_bell, 1)
    }

    private fun createCountDownTimer(initialMillis : Long): CountDownTimer {
        return object :CountDownTimer(initialMillis,1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()
    }

    private  fun startCountDown() {
        currentTimer = createCountDownTimer(seekBar.progress * 60 * 1000L)
        currentTimer?.start()

        tickingSoundId?.let { soundId ->
            soundpool.play(soundId,1F,1F,0,-1,1F)
        }
    }
    private fun stopCountDown() {
        currentTimer?.cancel()
        currentTimer = null
        soundpool.autoPause()
    }

    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)

        bellSoundId?.let { soundId ->
            soundpool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }
}