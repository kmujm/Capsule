package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ResetProgressActivity : AppCompatActivity() {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저

    // Todo: 리셋 도중 back button 누를 경우
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_progress)

    }
    override fun onResume() {
        super.onResume()

        if (user!=null) {
            var uid = user!!.uid
            // capsule 삭제
            myRef.child(uid).get().addOnSuccessListener {
                it.children.forEach {
                    if (it.key.toString() != "Info") {
                        removeCapsule(uid, it.key.toString())
                    }
                }
                // capsule 삭제 완료시 완료 화면으로 이동
                val intent = Intent(this, ResetCompleteActivity::class.java)
                Handler().postDelayed({ startActivity(intent)}, 1000L)
            }
        }
    }

    private fun removeCapsule(uid: String, capsuleKey: String) {
        myRef.child("${uid}").child("${capsuleKey}").removeValue()
    }

}