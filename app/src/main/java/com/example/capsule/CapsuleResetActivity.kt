package com.example.capsule

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


class CapsuleResetActivity : AppCompatActivity() {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저

    private lateinit var uid:String
    private lateinit var userNickname:String
    private var userCount:Int = 0   // capsule 개수

    private val tvUsername: TextView by lazy {
        findViewById(R.id.user_name)
    }
    private val tvMessage: TextView by lazy {
        findViewById(R.id.message)
    }
    private val btnProgress: Button by lazy {
        findViewById(R.id.btn_progress)
    }
    private val btnCancel: Button by lazy {
        findViewById(R.id.btn_cancel)
    }
    private val btnBack: Button by lazy {
        findViewById(R.id.btn_Back)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsule_reset)

        // uid = user!!.uid
        initUsername()
        initMessage()
        initProgressButton()
        initCancelButton()
        initBackButton()
    }

    private fun initUsername() {
        myRef.child("asdfifeiofjn1233").child("Info").get().addOnSuccessListener {
            userNickname = it.child("nickname").getValue<String>().toString()

            // 실제 Username 으로 바꿈
            var content: String = tvUsername.text.toString()
            content = content.replace("Username",userNickname)
            val spannableString = SpannableString(content)

            // Username 부분만 bold로 변경
            val start = content.indexOf(userNickname)
            val end = start + userNickname.length

            spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tvUsername.setText(spannableString)
        }
    }

    private fun initMessage() {
        myRef.child("asdfifeiofjn1233").get().addOnSuccessListener {
            it.children.forEach {
                if (it.key.toString() != "Info") userCount+=1
            }

            // 실제 capsule 개수로 바꿈
            var content: String = tvMessage.text.toString()
            content = content.replace("28",userCount.toString())
            val spannableString = SpannableString(content)

            // capsule 개수 부분만 색 변경
            val start = content.indexOf(userCount.toString())
            val end = start + userCount.toString().length

            spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#E04834")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tvMessage.setText(spannableString)
        }
    }

    private fun initProgressButton() {
        btnProgress.setOnClickListener {
            val intent = Intent(this, ResetCheckPWActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initCancelButton() {
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun initBackButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}