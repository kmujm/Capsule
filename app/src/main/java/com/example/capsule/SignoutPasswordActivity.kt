package com.example.capsule

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignoutPasswordActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private val auth by lazy {
        Firebase.auth.currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signout_password)

        // db 래퍼런스
        initSubmitButton()
        db = Firebase.database.reference.child("Users").child(auth!!.uid)

    }

    private fun initSubmitButton() {
        val submitButton = findViewById<AppCompatButton>(R.id.spSubmitButton)
        submitButton.setOnClickListener {
            Log.d(TAG, "onClicked!")
            val passwordEditText = findViewById<EditText>(R.id.spPasswordEditText)
            db.child(INFO).get().addOnSuccessListener {
                if (it != null) {
                    val text = it.value as HashMap<String, String>
                    val curPassword = text.get("password").toString()
                    Log.d(TAG, "DB pwd : ${curPassword}")
                    Log.d(TAG, "edit pwd : ${passwordEditText.text}")
                    if (curPassword == passwordEditText.text.toString()) {
                        // 비밀번호가 일치하면 계정
                        // 회월탈퇴
                        // TODO 리얼타임 DB에서도 지우기
                        if (auth != null) {
                            auth!!.delete()
                            db.removeValue()
                            val intent = Intent(this, SignoutSuccessActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "로그인정보가 이상합니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 비밀번호가 일치하지 않는경우
                        // 다시 입력
                        // 커스텀 다이얼로그 열기
                        passwordEditText.text.clear()
                        setDialog()
                    }
                }

            }
        }

    }

    private fun setDialog() {
        val dialogView = View.inflate(this, R.layout.nickname_dialog, null)
        val nickTitle = dialogView.findViewById<TextView>(R.id.title)
        val nickText = dialogView.findViewById<TextView>(R.id.message)
        nickTitle.text = "실패"
        nickText.text = "비밀번호를\n 다시 설정해주세요! "
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


    companion object {
        const val TEMP_AUTH_UID = "asdfifeiofjn1233"
        const val TAG = "SignoutPasswordActivity"
        const val INFO = "Info"
    }
}