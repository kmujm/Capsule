package com.example.capsule

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.nio.channels.spi.AbstractSelectionKey

// todo: adapter와 recyclerview 연결
class CapsuleListActivity : AppCompatActivity() {
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    var user = FirebaseAuth.getInstance().currentUser

    lateinit var userData:String
    lateinit var date:String
    lateinit var title:String
    lateinit var photoUri: Uri

    private var capsuleKey: MutableList<String> = mutableListOf()   //capsule key 들만 모아놓은 배열


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsule_list)

        if (user != null) {
            var uid = user!!.uid

            myRef.child("asdfifeiofjn1233").child("capsuleKey").get().addOnSuccessListener {
                capsuleKey = it.value.toString().replace("[","").replace("]","").replace(" ", "").split(",") as MutableList<String>
                capsuleKey.removeAt(0)    // 맨 앞 null delete
                // capsule 가져옴
                for (capsule in capsuleKey) {
                    myRef.child("asdfifeiofjn1233").child(capsule).get().addOnSuccessListener {
                        // capsule 별 date, title, detectImage 저장
                        date = it.child("date").getValue<String>().toString()
                        title = it.child("title").getValue<String>().toString()
                        photoUri = it.child("detectImage").getValue<String>()!!.toUri()
                        Log.i("firebase", date)
                        Log.i("firebase", title)
                        Log.i("firebase", photoUri.toString())
                    }.addOnFailureListener{
                        Log.e("firebase", "Error getting data", it)
                    }
                }
            }
            // todo:  .child(uid)로 수정해야 함
        }
    }
}