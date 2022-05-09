package com.example.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {
    private val photoList = mutableListOf<Uri>()

    private val photoImageView: ImageView by lazy {
        findViewById(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById(R.id.backgroundPhotoImageView)
    }

    private var currentPosition = 0

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        Log.d("PhotoFrameActivity", "onCreate!!!")

        getPhotoUriFromIntent()
    }

    fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                Log.d("PhotoFrameActivity", "5 seconds")
//                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[currentPosition])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("PhotoFrameActivity", "onStart!!! timer start")
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        Log.d("PhotoFrameActivity", "onStop!!! timer cancel")
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PhotoFrameActivity", "onDestroy!!! timer cancel")
        timer?.cancel()
    }

}