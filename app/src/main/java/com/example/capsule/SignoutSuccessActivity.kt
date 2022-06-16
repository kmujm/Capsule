package com.example.capsule

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignoutSuccessActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signout_success)

        initBackButton()
        initSubmitButton()

    }

    private fun initSubmitButton() {
        val submitButton = findViewById<Button>(R.id.ssSubmitButton)
        submitButton.setOnClickListener {
            finish()
        }
    }

    private fun initBackButton() {
        val backButton = findViewById<Button>(R.id.ssBackButton)
        backButton.setOnClickListener {
            finish()
        }
    }

}
