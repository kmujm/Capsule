package com.example.capsule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible

class NicknameActivity : AppCompatActivity(), TextWatcher {
    private var wordCount: Int = 0
    private val nickName: EditText by lazy {
        findViewById(R.id.nickname)
    }
    private val moreThanOneText: TextView by lazy {
        findViewById(R.id.moreThanOne)
    }
    private val submitButton: AppCompatButton by lazy {
        findViewById(R.id.submitButton)
    }
    private val wordCounter: TextView by lazy {
        findViewById(R.id.wordCounter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        nickName.addTextChangedListener(this)
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val nicknameFromUser = nickName.text.toString()
        wordCount = nicknameFromUser.length

        if (!nicknameFromUser.isBlank()) {
            submitButton.background = getDrawable(R.drawable.activate_button_background)
            moreThanOneText.isVisible = false
            moreThanOneText.isEnabled = true
        } else {
            submitButton.background = getDrawable(R.drawable.inactivate_button_background)
            submitButton.isEnabled = false
            moreThanOneText.isVisible = true
        }
        wordCounter.text = "${wordCount}/10"
    }

    override fun afterTextChanged(p0: Editable?) {
        // TODO("Not yet implemented")
    }
}