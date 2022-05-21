package com.example.pushnotificationreceiver

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging

/*
Sending Message

{
  "message": {
    "data": {
      "title": "Test title!!",
      "message": "Test message!!",
      "type": "NORMAL"
    },
    "token": "eXG0CTdISOCiNYRchy9Aaz:APA91bFgDpVJBlQMeELEY0mEbhhni5fY-2Qz8oBNPgl240UAZUhjqJyonY7kuLznWG9nfLzV7qDuzgnt77SixsnMUW6WEKwEDcjM_fAsYGeupgFdJDX8ZziZJfa7xQCGI3ECH8XCx0yE"
  }
}
 */
class MainActivity : AppCompatActivity() {
    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                firebaseToken.text = task.result
                Log.d("TOKEN", task.result)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if(isNewIntent) {
                    "(으)로 갱신했습니다"
                } else {
                    "(으)로 실행했습니다"
                }
    }
}