package jp.co.info.ais.a03secretdiary

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    private val diaryEditText: EditText by lazy {
        findViewById(R.id.diaryEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        initDetailEditText()
    }

    private fun initDetailEditText() {
        diaryEditText.setText(getSharedPreferences("diary", MODE_PRIVATE).getString("detail", ""))

        val runnable = Runnable {
            getSharedPreferences("diary", MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
                Log.d("DiaryActivity", "SAVED :: ${diaryEditText.text.toString()}")
            }
        }

        diaryEditText.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }

}