package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RemoveAccountActivity : AppCompatActivity() {
    private val TAG = "RemoveAccountActivity"

    private val backBtn: Button by lazy {
        findViewById(R.id.btn_RemoveBack)
    }

    private val removeInfo: TextView by lazy {
        findViewById(R.id.RemoveInfo)
    }

    private val userNameView: TextView by lazy {
        findViewById(R.id.RemoveUserName)
    }

    private val userEmailView: TextView by lazy {
        findViewById(R.id.RemoveEmail)
    }

    private val myCapsuleNum: TextView by lazy {
        findViewById(R.id.MyCapsuleNum)
    }

    private val myCapsuleTime: TextView by lazy {
        findViewById(R.id.CapsuleTimeNum)
    }

    private val removeAccountBtn: AppCompatButton by lazy {
        findViewById(R.id.btn_RemoveAccount)
    }

    private val removeCancelBtn: AppCompatButton by lazy {
        findViewById(R.id.btn_RemoveCancel)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var UID: String
    private lateinit var userNickName: String
    private lateinit var userEmail: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_account)
        database = Firebase.database.reference
        auth = Firebase.auth
        initMyInfo()
        initAccountRemoveBtn()
        initCancelBtn()
        initBackBtn()
    }

    private fun initBackBtn() {
        backBtn.setOnClickListener {
            // 뒤로가기 버튼 클릭시 동작
            finish()
        }
    }

    private fun initCancelBtn() {
        removeCancelBtn.setOnClickListener {
            // 취소 버튼 클릭시 동작
            finish()
        }
    }

    private fun initAccountRemoveBtn() {
        removeAccountBtn.setOnClickListener {
            // 회원 탈퇴 버튼 클릭시 동작
            val intent = Intent(this, RemoveReasonActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMyCapsule() {
        var cnt = 0
        UID = auth.currentUser!!.uid
        val myRef = database.child("Users").child(UID)
        myRef.get().addOnSuccessListener {
            it.children.forEach {
                if (it.key.toString() != "Info") {
                    cnt += 1
                }
            }
            removeInfo.text = "${userNickName} 님의 \n계정과 캡슐 ${cnt}개가 \n사라집니다."
            myCapsuleNum.text = cnt.toString() + " 개"
            myCapsuleTime.text = "NN h"
        }.addOnFailureListener {
            //
        }
    }

    private fun initMyInfo() {
        val user = auth.currentUser
        if (user != null) {
            UID = user.uid
            database.get().addOnSuccessListener {
                val userInfo = it.child("Users").child(UID).child("Info").value as HashMap<*, *>
                userNickName = userInfo.get("nickname").toString()
                userEmail = userInfo.get("email").toString()
                userNameView.text = userNickName
                userEmailView.text = userEmail
                initMyCapsule()
            }.addOnFailureListener {
                Log.d(TAG, "Fail getting user nickname data", it)
            }
        }
    }

}
