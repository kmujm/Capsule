package com.example.capsule

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NicknameActivity : AppCompatActivity() {
    private var wordCount: Int = 0
    private val nickName: EditText by lazy {
        findViewById(R.id.nickname)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        countWord()
    }

    private fun countWord() {
    }
}