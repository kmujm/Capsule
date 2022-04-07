package com.example.capsule

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class EditProfileActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.back_button)
    }

    private val editNickname : EditText by lazy{
        findViewById(R.id.edit_nickname)
    }

    private val validNickname : TextView by lazy{
        findViewById(R.id.valid_nickname)
    }

    private val invalidNickname : TextView by lazy{
        findViewById(R.id.invalid_nickname)
    }

    private val editEmail : EditText by lazy{
        findViewById(R.id.edit_id)
    }

    private val completeButton : Button by lazy{
        findViewById(R.id.complete_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        backButton.setOnClickListener{
            finish() // 이전화면으로 이동
        }

        // 닉네임 입력칸 처리
        editNickname.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> // editText의 포커스가 변경될 때의 event listener
            if(!hasFocus){
                if(isNicknameValid(editNickname.text.toString())){
                    editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, null, null)
                    editNickname.background = this.resources.getDrawable(R.drawable.edittext_background)
                    invalidNickname.isVisible = false
                    validNickname.isVisible = true
                } else{
                    val alert = editNickname.context.resources.getDrawable( R.drawable.alert_mark )
                    editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, alert, null) // alert_mark를 edittext의 오른쪽 부분에 나타나게 함
                    editNickname.background = this.resources.getDrawable(R.drawable.edittext_alert_background)
                    invalidNickname.isVisible = true
                    validNickname.isVisible = false
                }
            }
        }
    }

    //nickname의 유효성 검사
    private fun isNicknameValid(nickname : String) : Boolean{
        if(nickname.isNotEmpty() && nickname.length <= 10){ // 글자 수 제한에 맞는지 확인
            // TODO 사용중인 닉네임인지 확인
            // TODO 사용중인 닉네임이면 return false
            // TODO 사용중인 닉네임이 아니면 return true
            return true // 수정 필요
        }
        return false
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