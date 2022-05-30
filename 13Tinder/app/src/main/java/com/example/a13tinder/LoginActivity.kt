package com.example.a13tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.a13tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        initEmailAndPasswordEditText()
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        finish()
                    }else {
                        Toast.makeText(this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initEmailAndPasswordEditText() {
        binding.emailEditText.addTextChangedListener {
            checkValidate()
        }

        binding.passwordEditText.addTextChangedListener {
            checkValidate()
        }
    }

    private fun checkValidate() {
        val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
        binding.loginButton.isEnabled = enable
        binding.signUpButton.isEnabled = enable
    }

    private fun getInputPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    private fun getInputEmail(): String {
        return binding.emailEditText.text.toString()
    }


}