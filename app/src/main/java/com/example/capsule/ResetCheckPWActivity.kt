package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton

class ResetCheckPWActivity : AppCompatActivity(), TextWatcher {
    private val pw: EditText by lazy {
        findViewById(R.id.pw)
    }
    private val btnComplete: AppCompatButton by lazy {
        findViewById(R.id.btn_complete)
    }
    private val btnBack: Button by lazy {
        findViewById(R.id.btn_Back)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_check_pw)

        pw.addTextChangedListener(this)

        initBackButton()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var currentPW = pw.text.toString()

        if (currentPW.length >= 8) {   // 입력한 비밀번호가 8자 이상이면 완료 버튼 활성화
            btnComplete.background = getDrawable(R.drawable.activate_button_background)
        } else {
            btnComplete.background = getDrawable(R.drawable.inactivate_button_background)
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initBackButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}