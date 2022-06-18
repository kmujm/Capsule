package com.example.capsule

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class PrivacyActivity : AppCompatActivity() {
    private val userName: EditText by lazy {
        findViewById(R.id.PrivacyUserName)
    }
    private val userEmail: EditText by lazy {
        findViewById(R.id.PrivacyEmail)
    }
    private val backButton: Button by lazy {
        findViewById(R.id.btn_PrivacyBack)
    }
    private val privacyEditProfile : Button by lazy{
        findViewById(R.id.PrivacyEditProfile)
    }
    private val myCapsuleButton : Button by lazy{
        findViewById(R.id.PrivacyVideo)
    }

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    private var auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser    // 현재 로그인한 유저

    private lateinit var Email:String
    private lateinit var Nickname:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        initPrivacyEditProfileButton()
        initBackButton()
        initMyCapsuleButton()
    }

    private fun initBackButton() {
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initMyCapsuleButton() {
        myCapsuleButton.setOnClickListener {
            val intent = Intent(this, CapsuleListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        if (user!=null) {
            var uid = user!!.uid
            // 로그인한 유저의 email, nickname 가져옴
            myRef.child(uid).child("Info").get().addOnSuccessListener {
                Email = it.child("email").getValue<String>().toString()
                Nickname = it.child("nickname").getValue<String>().toString()

                userEmail.setText(Email)
                userName.setText(Nickname)
            }
        }
    }

    private fun initPrivacyEditProfileButton(){
        privacyEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}