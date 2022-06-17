package com.example.capsule

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class NicknameActivity : AppCompatActivity(), TextWatcher {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var database: DatabaseReference
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
        findViewById(R.id.NicknameSubmitButton)
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
        database = Firebase.database.reference

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
                        "email" to email,
                        "nickname" to nicknameText,
                        "password" to password
                    )
                    database.child("Users").child(UID).child("Info").setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e -> Log.d("error", e.toString()) }

//                    val user = hashMapOf(
//                        "Email" to email,
//                        "Nickname" to nicknameText
//                    )
//                    db.collection("users").document(UID)
//                        .set(user)
//                        .addOnSuccessListener {
//                            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
//                        }
//                        .addOnFailureListener { e -> Log.d("error", e.toString()) }
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // 회원가입 실패
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initsubmitButton() {
        submitButton.setOnClickListener {
            Log.d(TAG, "완료 버튼 누름")
            nicknameText = nickName.text.toString()
            checkNickname(nicknameText)
        }
    }

    private fun setDialog(curNickname: String) {
        val dialogView = View.inflate(this, R.layout.nickname_dialog, null)
        val nickTitle = dialogView.findViewById<TextView>(R.id.title)
        val nickText = dialogView.findViewById<TextView>(R.id.message)
        nickTitle.text = "닉네임 설정"
        nickText.text = "${curNickname}은 \n사용 불가능한 닉네임이에요!"
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogOkButton: AppCompatButton = dialogView.findViewById(R.id.positiveButton)
        dialogOkButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun checkNickname(curNickname: String) {
//        db.collection("users")
//            .whereEqualTo("Nickname", curNickname)
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    if (curNickname == document.data["Nickname"].toString()) {
//                        setDialog(curNickname)
//                        nickName.backgroundTintList =
//                            ContextCompat.getColorStateList(this, R.color.cost)
//                        Log.d(TAG, "${curNickname}은 이미 존재합니다")
//                        return@addOnSuccessListener
//                    }
//                }
//                setAuth()
//            }
//            .addOnFailureListener { exception ->
//                Log.d("에러다", exception.toString())
//                return@addOnFailureListener
//            }
        database.child("Users").get().addOnSuccessListener {
            Log.d(TAG, it.toString())
            it.children.forEach {
                Log.d(TAG, it.toString())
                val userInfo = it.child("Info").value as HashMap<*, *>
                Log.d(TAG, userInfo.toString())
                Log.d(TAG, userInfo.get("nickname").toString())
                if (userInfo.get("nickname").toString() == curNickname) {
                    setDialog(curNickname)
                    nickName.backgroundTintList =
                        ContextCompat.getColorStateList(this, R.color.cost)
                    Log.d(TAG, "${curNickname}은 이미 존재합니다")
                    return@addOnSuccessListener

                }
            }
            setAuth()
        }.addOnFailureListener { exception ->
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
            submitButton.isEnabled = true
            moreThanOneText.visibility = View.INVISIBLE
        } else {
            submitButton.background = getDrawable(R.drawable.inactivate_button_background)
            submitButton.isEnabled = false
            moreThanOneText.visibility = View.VISIBLE
        }
        wordCounter.text = "${wordCount}/10"
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { // 현재 포커스된 뷰의 영역이 아닌 다른 곳을 클릭 시 키보드를 내리고 포커스 해제
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}