package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResetProgressActivity : AppCompatActivity() {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저

    // Todo: 리셋 도중 back button 누를 경우
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_progress)

        // capsule 삭제
        myRef.child("ResetTestUser").get().addOnSuccessListener {
            it.children.forEach {
                if (it.key.toString() != "Info") {
                    removeCapsule("ResetTestUser", it.key.toString())
                }
            }
        }
    }

    private fun removeCapsule(uid: String, capsuleKey: String) {
        myRef.child("${uid}").child("${capsuleKey}").removeValue()
    }

}