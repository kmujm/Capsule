package com.example.capsule

import android.content.Intent
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
            val intent = Intent(this,LoginActivity::class.java)
            // 액티비티 스택을 모두 지움
            finishAffinity()
            startActivity(intent)
            finish()
        }
    }

    private fun initBackButton() {
        val backButton = findViewById<Button>(R.id.ssBackButton)
        backButton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            // 액티비티 스택을 모두 지움
            finishAffinity()
            startActivity(intent)
            finish()
        }
    }

}
