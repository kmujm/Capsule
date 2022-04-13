package com.example.capsule

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NicknameActivity : AppCompatActivity(), TextWatcher {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private var wordCount: Int = 0
    private lateinit var nicknameText: String
    private val backButton: ImageButton by lazy {
        findViewById(R.id.backButton)
    }
    private val nickName: EditText by lazy {
        findViewById(R.id.nickname)
    }
    private val moreThanOneText: TextView by lazy {
        findViewById(R.id.moreThanOne)
    }
    private val submitButton: AppCompatButton by lazy {
        findViewById(R.id.submitButton)
    }
    private val wordCounter: TextView by lazy {
        findViewById(R.id.wordCounter)
    }

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var UID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        auth = Firebase.auth

        nickName.addTextChangedListener(this)

        email = intent.getStringExtra("userEmail").toString()
        password = intent.getStringExtra("userPassword").toString()

        Log.d("NICKNAMEACTIVITY", email.toString())
        Log.d("NICKNAMEACTIVITY", password.toString())
        initsubmitButton()
        initbackButton()

    }

    private fun initbackButton() {
        backButton.setOnClickListener {
            finish()
        }

    }

    private fun initsubmitButton() {
        submitButton.setOnClickListener {
            nicknameText = nickName.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //  회원가입 성공
                        UID = auth.currentUser?.uid.toString()
                        Log.d("UID!!!!!", UID)
                        val user = hashMapOf(
                            "Email" to email,
                            "Nickname" to nicknameText
                        )
                        db.collection("users").document(UID)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e -> Log.d("error", e.toString()) }
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var nicknameFromUser = nickName.text.toString()
        wordCount = nicknameFromUser.length

        if (!nicknameFromUser.isBlank()) {
            submitButton.background = getDrawable(R.drawable.activate_button_background)
            moreThanOneText.isVisible = false
            moreThanOneText.isEnabled = true
        } else {
            submitButton.background = getDrawable(R.drawable.inactivate_button_background)
            submitButton.isEnabled = false
            moreThanOneText.isVisible = true
        }
        wordCounter.text = "${wordCount}/10"
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}