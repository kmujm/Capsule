package com.example.capsule

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class EditProfileActivity : AppCompatActivity() {

    private val backButton : ImageButton by lazy{
        findViewById(R.id.back_button)
    }

    private val editNickname : EditText by lazy{
        findViewById(R.id.edit_nickname)
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
                val nickname = editNickname.text.toString()
                if(isNicknameValid(nickname)){
                    editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, null, null)
                    editNickname.background = this.resources.getDrawable(R.drawable.edittext_background)
                    completeButton.background = this.resources.getDrawable(R.drawable.activate_button_background)
                    completeButton.isEnabled = true
                } else{
                    val alert = editNickname.context.resources.getDrawable( R.drawable.alert_mark )
                    editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, alert, null)
                    editNickname.background = this.resources.getDrawable(R.drawable.edittext_alert_background)
                    completeButton.background = this.resources.getDrawable(R.drawable.inactivate_button_background)
                    completeButton.isEnabled = false
                }
            }
        }

        // 완료 버튼이 눌렸을 경우
        completeButton.setOnClickListener {
            val nickname = editNickname.text.toString()
            if(isNicknameValid(nickname) && isNicknameUsable(nickname)){ // 닉네임이 유효한 경우
                editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, null, null)
                editNickname.background = this.resources.getDrawable(R.drawable.edittext_background)
                // alert dialog를 띄운다.
                NicknameDialog(this).setTitle("닉네임 설정").setMessage(editNickname.text.toString() + "은 사용 가능한 닉네임이에요!").setPositiveButton("확인"){
                    //TODO 수정한 닉네임 서버에 저장
                    //TODO 개인정보 화면으로 이동
                    Toast.makeText(this,"프로필 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }.show()
            } else{ // 닉네임이 유효하지 않은 경우
                val alert = editNickname.context.resources.getDrawable( R.drawable.alert_mark )
                editNickname.setCompoundDrawablesWithIntrinsicBounds( null, null, alert, null) // alert_mark를 edittext의 오른쪽 부분에 나타나게 함
                editNickname.background = this.resources.getDrawable(R.drawable.edittext_alert_background)
                // alert dialog를 띄운다.
                val ad = NicknameDialog(this).setTitle("닉네임 설정").setMessage(editNickname.text.toString() + "은 사용 불가능한 닉네임이에요!")
                ad.setPositiveButton("확인"){
                    ad.dismiss()
                }.show()
                completeButton.background = this.resources.getDrawable(R.drawable.inactivate_button_background)
            }
        }

        //TODO 이메일 서버에서 가져와서 보여주기
    }
    // nickname의 유효성 검사
    private fun isNicknameValid(nickname : String) : Boolean{
        return nickname.isNotEmpty() && nickname.length <= 10 // 글자 수 제한에 맞는지 확인
    }

    // 닉네임이 사용 가능한지 확인
    private fun isNicknameUsable(nickname: String) : Boolean{
        // TODO 사용중인 닉네임인지 확인
        // TODO 사용중인 닉네임이면 return false
        // TODO 사용중인 닉네임이 아니면 return true
        return false // TODO 수정 필요
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