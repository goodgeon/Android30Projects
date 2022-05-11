package com.example.recorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var state: State = State.BEFORE_RECORDING

    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }
}