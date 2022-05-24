package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CapsuleListActivity : AppCompatActivity() {
    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsule_list)
//        realtime database 연동 확인
//        val database = Firebase.database
//        val myRef = database.getReference("message")
//        myRef.setValue("Success")
        mDatabase.child("Users").child(userId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}