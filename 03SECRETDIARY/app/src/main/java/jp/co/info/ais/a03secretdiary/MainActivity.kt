package jp.co.info.ais.a03secretdiary

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1).apply {
            maxValue = 9
            minValue = 0
        }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2).apply {
            maxValue = 9
            minValue = 0
        }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3).apply {
            maxValue = 9
            minValue = 0
        }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initOpenButton()
        initChangePasswordButton()
    }

    private fun initNumberPicker() {
        numberPicker1
        numberPicker2
        numberPicker3
    }

    private fun initOpenButton() {
        openButton.setOnClickListener {
            if(changePasswordMode) {
                Toast.makeText(this, "Password change mode", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreference = getSharedPreferences("password", MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if(passwordPreference.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorPopup()
            }
        }
    }

    private fun initChangePasswordButton() {
        changePasswordButton.setOnClickListener {
            val sharedPreference = getSharedPreferences("password", MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if(changePasswordMode) {
                sharedPreference.edit {
                    this.putString("password", passwordFromUser)
                    commit()
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
                Toast.makeText(this,"Password changed", Toast.LENGTH_SHORT)
            } else {
                if(sharedPreference.getString("password", "000") == passwordFromUser) {
                    changePasswordMode = true
                    changePasswordButton.setBackgroundColor(Color.RED)
                    Toast.makeText(this, "Input new password and click again", Toast.LENGTH_SHORT).show()
                } else {
                    showErrorPopup()
                }
            }

        }
    }

    private fun showErrorPopup() {
        AlertDialog.Builder(this)
            .setTitle("Fail")
            .setMessage("Invalid password")
            .setPositiveButton("Confirm") {_, _, -> }
            .create()
            .show()
    }
}