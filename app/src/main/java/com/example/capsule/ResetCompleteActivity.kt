package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResetCompleteActivity : AppCompatActivity() {
    private val resetDate: TextView by lazy {
        findViewById(R.id.reset_date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_complete)

        setResetDateMsg()
    }

    private fun setResetDateMsg() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)

        // 오늘 날짜로 변경
        resetDate.text = "${formatted} 리셋"
    }
}