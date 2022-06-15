package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_reason)
        initCheckBox()

    }


    private fun initCheckBox() {
        myCheck1.setOnCheckedChangeListener(listener)
        myCheck2.setOnCheckedChangeListener(listener)
        myCheck3.setOnCheckedChangeListener(listener)
        myCheck4.setOnCheckedChangeListener(listener)
    }
}