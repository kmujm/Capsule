package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

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

    val objectDetectionButton : Button by lazy{
        findViewById(R.id.objectDetectionButton)
    }

    val CapsuleListButton : Button by lazy{
        findViewById(R.id.capsuleListButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initObjectDetectionButton()
    }

    private fun initObjectDetectionButton(){
        objectDetectionButton.setOnClickListener {
            val intent = Intent(this, ObjectDetectionActivity::class.java)
            startActivity(intent)
        }
    }
}