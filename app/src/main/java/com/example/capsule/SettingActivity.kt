package com.example.capsule

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class SettingActivity : AppCompatActivity() {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저
    private lateinit var userEmail:String

    private val infoButton: Button by lazy {
        findViewById(R.id.btn_SettingInfo)
    }
    private val backButton: Button by lazy {
        findViewById(R.id.btn_SettingBack)
    }
    private val logoutButton: Button by lazy {
        findViewById(R.id.btn_SettingLogout)
    }

    private val signDeleteButton: Button by lazy {
        findViewById(R.id.btn_SettingWithdrawal)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        var uid = user!!.uid
        // 로그인한 유저의 email 가져옴
        myRef.child("asdfifeiofjn1233").child("Info").get().addOnSuccessListener {
            userEmail = it.child("email").getValue<String>().toString()
        }

        initInfoButton()
        initBackButton()
        initLogoutButton()
        initSignDeleteButton()
    }

    private fun initSignDeleteButton() {
        signDeleteButton.setOnClickListener {
            val intent = Intent(this, RemoveAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initInfoButton() {
        infoButton.setOnClickListener {
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBackButton() {
        backButton.setOnClickListener {
            finish()
        }

    }

    private fun initLogoutButton() {
        logoutButton.setOnClickListener {
            initDialog()
        }
    }

    private fun initDialog() {
        val dlg = Dialog(this)
        dlg.setContentView(R.layout.logout_dialog)
        dlg.setCancelable(false)
        dlg.window?.setBackgroundDrawable(Drawable.createFromPath("@drawable/alert_dialog_background"))

        // dialog title
        var title: TextView = dlg.findViewById(R.id.title)
        title.text = "로그아웃"
        // dialog message
        var message : TextView = dlg.findViewById(R.id.content)
        message.text = "${userEmail} \n 로그아웃 하시겠습니까?"
        // 취소 버튼 클릭
        var btnCancel : Button = dlg.findViewById(R.id.cancel)
        btnCancel.setOnClickListener {
            dlg.dismiss()
        }
        // 로그아웃 버튼 클릭
        var btnLogout : Button = dlg.findViewById(R.id.logout)
        btnLogout.setOnClickListener {
            dlg.dismiss()
            auth.signOut()
            // 로그인 페이지로 이동 + 토스트 메시지 띄워줌
            val intent = Intent(this, LoginActivity::class.java)
            makeToast()
            startActivity(intent)
            finish()
        }
        dlg.show()
    }

    private fun makeToast() {
        val loginView = View.inflate(this, R.layout.activity_login, null)
        val toastView = View.inflate(this, R.layout.custom_toast, null)

        // 로그인 화면에 토스트 메시지 띄워줌
        var toast = Toast(loginView.context)
        toast.view = toastView
        toast.show()
    }
}