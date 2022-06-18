package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


class ResetCompleteActivity : AppCompatActivity() {
    private val resetDate: TextView by lazy {
        findViewById(R.id.reset_date)
    }
    private val btnComplete: Button by lazy {
        findViewById(R.id.btn_complete)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_complete)
        AndroidThreeTen.init(this);

        setResetDateMsg()
        initCompleteButton()
    }

    private fun setResetDateMsg() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted = current.format(formatter)

        // 오늘 날짜로 변경
        resetDate.text = "${formatted} 리셋"
    }

    private fun initCompleteButton() {
        // 완료 버튼 클릭시 설정 화면으로 이동
        btnComplete.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}