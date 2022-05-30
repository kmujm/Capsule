package com.example.capsule

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val alarmButton : ImageButton by lazy{
        findViewById(R.id.alarm)
    }

    val mainPageButton : ImageButton by lazy{
        findViewById(R.id.mainPage)
    }

    val settingButton : ImageButton by lazy{
        findViewById(R.id.settings)
    }

    val greeting : TextView by lazy{
        findViewById(R.id.greeting)
    }

    var username = "USERNAME" // 임시 사용

    val objectDetectionButton : Button by lazy{
        findViewById(R.id.objectDetectionButton)
    }

    val CapsuleListButton : Button by lazy{
        findViewById(R.id.capsuleListButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val content = "${username} 님,\n오늘도 캡슐하세요!"
        val spannableString = SpannableString(content)
        val start_idx = 0
        val end_idx = username.length

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start_idx, end_idx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        greeting.text = spannableString

        initObjectDetectionButton()
    }

    private fun initObjectDetectionButton(){
        objectDetectionButton.setOnClickListener {
            val intent = Intent(this, ObjectDetectionActivity::class.java)
            startActivity(intent)
        }
    }
}