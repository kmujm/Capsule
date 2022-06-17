package com.example.capsule

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ResetCheckPWActivity : AppCompatActivity(), TextWatcher {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저
    private lateinit var userPW:String     // 현재 로그인한 유저의 비밀번호

    private val pw: EditText by lazy {
        findViewById(R.id.pw)
    }
    private val btnComplete: AppCompatButton by lazy {
        findViewById(R.id.btn_complete)
    }
    private val btnBack: Button by lazy {
        findViewById(R.id.btn_Back)
    }

    private var clickable: Boolean = false
    private var clear: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_check_pw)

        pw.addTextChangedListener(this)

        initBackButton()
        initCompleteButton()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var currentPW = pw.text.toString()

        if (currentPW.length >= 8) {   // 입력한 비밀번호가 8자 이상이면 완료 버튼 활성화
            btnComplete.background = getDrawable(R.drawable.activate_button_background)
            clickable = true
        } else {
            btnComplete.background = getDrawable(R.drawable.inactivate_button_background)
            clickable = false
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    private fun initBackButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initCompleteButton() {
        btnComplete.setOnClickListener {
            if (clickable) {   // 완료 버튼이 활성화인 경우
                checkPW()
            }
        }
    }

    private fun checkPW() {
        if (user!=null) {
            var uid = user!!.uid
            // 로그인한 유저의 password 가져옴
            myRef.child(uid).child("Info").get().addOnSuccessListener {
                userPW = it.child("password").getValue<String>().toString()

                var inputPW = pw.text.toString()
                if (inputPW != userPW) {   // 잘못된 비밀번호를 입력한 경우
                    setDialog()
                    clear = false
                    // 버튼 비활성화 + 라인 색 변경
                    btnComplete.background = getDrawable(R.drawable.inactivate_button_background)
                    clickable = false
                    pw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.cost)

                    // 입력창 터치하면 다 지워지고 처음상태로 변경
                    resetPw()
                } else {
                    val intent = Intent(this, ResetProgressActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun resetPw() {
        pw.setOnClickListener {
            if (!clear) {
                pw.setText("")
                pw.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.catagory_icon_brown)
                clear = true
            }
        }
    }

    private fun setDialog() {
        val dlg = Dialog(this)
        dlg.setContentView(R.layout.pw_check_dialog)
        dlg.setCancelable(false)
        dlg.window?.setBackgroundDrawable(Drawable.createFromPath("@drawable/alert_dialog_background"))

        // dialog message
        var tvMessage : TextView = dlg.findViewById(R.id.content)
        var message = tvMessage.text.toString()
        val spannableString = SpannableString(message)

        // 비밀번호 문자열만 bold 변경
        var word = "비밀번호"
        val start = message.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvMessage.text = spannableString

        var btnOK : Button = dlg.findViewById(R.id.btn_ok)
        btnOK.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}