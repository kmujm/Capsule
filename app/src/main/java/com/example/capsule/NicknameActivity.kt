package com.example.capsule

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
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

    private fun setAuth() {
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // 회원가입 실패
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initsubmitButton() {
        submitButton.setOnClickListener {
            nicknameText = nickName.text.toString()
            checkNickname(nicknameText)
        }
    }

    private fun setDialog(curNickname: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("닉네임 설정")
            .setMessage("${curNickname}은 사용 불가능한 닉네임이에요!")
            .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            })
        builder.show()
    }

    private fun checkNickname(curNickname: String) {
        db.collection("users")
            .whereEqualTo("Nickname", curNickname)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (curNickname == document.data["Nickname"].toString()) {
                        setDialog(curNickname)
                        nickName.backgroundTintList =
                            ContextCompat.getColorStateList(this, R.color.cost)
                        Log.d(TAG, "${curNickname}은 이미 존재합니다")
                        return@addOnSuccessListener
                    }
                }
                setAuth()
            }
            .addOnFailureListener { exception ->
                Log.d("에러다", exception.toString())
                return@addOnFailureListener
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
            moreThanOneText.visibility = View.INVISIBLE
            moreThanOneText.isEnabled = true
        } else {
            submitButton.background = getDrawable(R.drawable.inactivate_button_background)
            submitButton.isEnabled = false
            moreThanOneText.visibility = View.VISIBLE
        }
        wordCounter.text = "${wordCount}/10"
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}