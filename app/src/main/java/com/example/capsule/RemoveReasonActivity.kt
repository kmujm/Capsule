package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox

class RemoveReasonActivity : AppCompatActivity() {
    private val myCheck1: AppCompatCheckBox by lazy {
        findViewById(R.id.ReasonCheck1)
    }

    private val myCheck2: AppCompatCheckBox by lazy {
        findViewById(R.id.ReasonCheck2)
    }

    private val myCheck3: AppCompatCheckBox by lazy {
        findViewById(R.id.ReasonCheck3)
    }

    private val myCheck4: AppCompatCheckBox by lazy {
        findViewById(R.id.ReasonCheck4)
    }

    private val inputReason: EditText by lazy {
        findViewById(R.id.ReasonInput)
    }

    private val nextBtn: AppCompatButton by lazy {
        findViewById(R.id.btn_ReasonNext)
    }

    private var cnt = 0

    private val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            when (buttonView.id) {
                R.id.ReasonCheck1 -> cnt += 1
                R.id.ReasonCheck2 -> cnt += 1
                R.id.ReasonCheck3 -> cnt += 1
                R.id.ReasonCheck4 -> cnt += 1
            }
        } else {
            when (buttonView.id) {
                R.id.ReasonCheck1 -> cnt -= 1
                R.id.ReasonCheck2 -> cnt -= 1
                R.id.ReasonCheck3 -> cnt -= 1
                R.id.ReasonCheck4 -> cnt -= 1
            }
        }
        if (cnt >= 1) {
            nextBtn.isEnabled = true
            nextBtn.background = getDrawable(R.drawable.red_button_background)
        } else {
            nextBtn.isEnabled = false
            nextBtn.background = getDrawable(R.drawable.inactivate_button_background)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_reason)
        initCheckBox()
        initNextButton()
    }

    private fun initNextButton() {
        nextBtn.setOnClickListener {
            val reasonFromUser = inputReason.text.toString()
            val intent = Intent(this, SignoutPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initCheckBox() {
        myCheck1.setOnCheckedChangeListener(listener)
        myCheck2.setOnCheckedChangeListener(listener)
        myCheck3.setOnCheckedChangeListener(listener)
        myCheck4.setOnCheckedChangeListener(listener)
    }

}