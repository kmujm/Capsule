package com.example.capsule

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.capsule.databinding.ActivitySignupBinding
import java.util.regex.Pattern

private lateinit var binding: ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var isEmailPass = false
    private var isPasswordPass = false
    private var isStateShowingPassword = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDetctEmailEditText()
        initView()


    }

    private fun initView() {
        // 제출버튼 OFF상태로 초기화
        offSubmitButton()
        clearPasswordEditView()

        // 비밀번호 SHOW ON/OFF 클릭 리스너
        binding.passwordEyeButton.setOnClickListener {
            if(isStateShowingPassword){ // 비밀번호 보이기 모드 -> 안보이기 모드
                binding.passwordEyeButton.apply {
                    isStateShowingPassword = false
                    background = getDrawable(R.drawable.eye_close)
                }
                binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT or  InputType.TYPE_TEXT_VARIATION_PASSWORD)

            }else { // 비밀번호 안보이기 모드 -> 보이기 모드
                binding.passwordEyeButton.apply {
                    isStateShowingPassword = true
                    background = getDrawable(R.drawable.eye_open)
                }
                binding.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
        }

        // 이메일 중복확인 버튼 클릭 리스터
        binding.checkEmailButton.setOnClickListener {
            val curEmail = binding.emailEditText.text.toString()
            // TODO 파이어베이스에서 이메일 리스트를 가져와서 현재 이메일과 비교함
            if( curEmail == TEST_EMAIL ) {
                // 중복이 일 시
                showEmailCheckErrorMessage()
                showEmailEditTextAlertIcon()
                offCheckEmailButton()
                offSubmitButton()
                isEmailPass = false
            }else {
                // 중복이 아닐 시
                isEmailPass = true
                showEmailCheckPassMessage()
                onCheckEamilButton()
            }
        }

        // 비밀번호 정규식 확인 리스너
        binding.checkPasswordEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentPasswordEditText = binding.passwordEditText.text.toString()
                val currentPasswordCheckEditText = binding.checkPasswordEditText.text.toString()
                when{
                    currentPasswordEditText.isNullOrBlank().not() && currentPasswordCheckEditText.isNullOrBlank().not() && isEmailPass  -> {
                        // 비밀번호입력창이 비어있지 않고, 비밀번호 확인창이 비어있지 않은 경우
                        // 완료 버튼을 활성화
                        onSubmitButton()
                    }
                    else -> {
                        offSubmitButton()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        // 완료버튼 눌렀을때 리스너
        binding.signupSubmitButton.setOnClickListener {
            Log.d(TAG,"SUBMIT ON CLICKED!!")
            // 두개가 일치하는지 확인
            if (isSamePassword().not()) {
                showPasswordErrorMessage()
                isPasswordPass = false
                return@setOnClickListener
            }

            // 비밀번호 정규식 확인
            if(isPasswordPattern().not()) return@setOnClickListener

            // 다음 두 상태 검사 후 다음 인탠트로 넘겨줌
            if(( isEmailPass && isPasswordPass).not()) return@setOnClickListener

            Toast.makeText(this,"닉네임 페이지로 이동합니다",Toast.LENGTH_SHORT).show()
        }

    }

    private fun clearPasswordEditView() {
        binding.passwordEditText.setOnClickListener {
            binding.passwordEditText.setText("")
            hidePasswordAlertIcon()
        }

        binding.checkPasswordEditText.setOnClickListener {
            binding.checkPasswordEditText.setText("")
            hidePasswordAlertIcon()
        }
    }

    private fun hidePasswordAlertIcon() {
        binding.passwordEditTextAlertImageView.isVisible = false
    }

    private fun showPasswordErrorMessage() {
        binding.checkPasswordMessageTextView.setText(CHECK_PASSWORD_ERROR_MESSAGE)
        binding.checkPasswordMessageTextView.setTextColor(ContextCompat.getColor(this,R.color.cost))
        binding.checkPasswordMessageTextView.isVisible= true

        binding.passwordEditTextAlertImageView.isVisible = true
    }


    private fun isPasswordPattern() : Boolean{
        val curPassword = binding.passwordEditText.text.toString()
        val passwordPattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = passwordPattern.matcher(curPassword)
        if (matcher.find()) {
            isPasswordPass = true
            return true
        }
        showPasswordErrorMessage()
        isPasswordPass = false
        return false
    }

    private fun isSamePassword() : Boolean{
        val curPassword = binding.passwordEditText.text.toString()
        val checkPassword = binding.checkPasswordEditText.text.toString()
        if (curPassword == checkPassword) {
            Log.d(TAG,"IS SAME!!")
            return true
        }
        Log.d(TAG,"IS NOT SAME!!")
        return false
    }

    private fun offSubmitButton() {
        binding.signupSubmitButton.apply {
            isEnabled = false
            background = getDrawable(R.drawable.inactivate_button_background)
        }
    }

    private fun onSubmitButton() {
        binding.signupSubmitButton.apply {
            isEnabled = true
            background = getDrawable(R.drawable.activate_button_background)
        }

    }

    private fun hideEmailEditTextAlertIcon() {
        binding.emailEditTextAlertImageView.isVisible = false
    }

    private fun showEmailEditTextAlertIcon() {
        binding.emailEditTextAlertImageView.isVisible = true
    }

    private fun showEmailCheckPassMessage() {
        binding.emailCheckMessageTextView.apply{
            setText(CHECK_EMAIL_PASS_MESSAGE)
            isVisible = true
        }
        binding.emailCheckMessageTextView.setTextColor(ContextCompat.getColor(this,R.color.secondary))
    }

    private fun showEmailCheckErrorMessage() {
        binding.emailCheckMessageTextView.apply {
            isVisible = true
            setText(CHECK_EMAIL_ERROR_MESSAGE)
        }
        binding.emailCheckMessageTextView.setTextColor(ContextCompat.getColor(this,R.color.cost))
    }


    // 아메일 정규식 확인을 위한 함수 초기화
    private fun initDetctEmailEditText() {
        // 이메일 정규식 판별을 위한 이벤트 리스너
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val editText = binding.emailEditText.text.toString()
                if(isEmailPattern(email = editText)){
                    onCheckEamilButton()
                }else {
                    offCheckEmailButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isEmailPass = false
                hideEmailPassOrErrorMessage()
                hideEmailEditTextAlertIcon()

                Log.d(TAG,isEmailPass.toString())
            }

        })
    }

    private fun hideEmailPassOrErrorMessage() {
        binding.emailCheckMessageTextView.isVisible = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun offCheckEmailButton() {
        binding.checkEmailButton.apply{
            isEnabled = false
            background = getDrawable(R.drawable.inactivate_button_background)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onCheckEamilButton() {
        binding.checkEmailButton.apply {
            isEnabled = true
            background = getDrawable(R.drawable.activate_button_background)
        }

    }

    //이메일 정규식 검사, 이메일이라면 true 아니라면 false 반환 함수
    private fun isEmailPattern(email : String?): Boolean {
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        if (email.isNullOrEmpty()) return false
        when{
            pattern.matcher(email).matches() -> return true
            else -> return false
        }
    }

    companion object {
        private const val TAG = "SignupActivity"
        private const val TEST_EMAIL = "test@gmail.com"
        private const val CHECK_EMAIL_ERROR_MESSAGE = "사용 할 수 없는 이메일입니다."
        private const val CHECK_EMAIL_PASS_MESSAGE = "사용 가능한 이메일입니다."
        private const val CHECK_PASSWORD_ERROR_MESSAGE = "비밀번호를 다시 확인해주세요."
        // 영문, 숫자 8 ~ 20자 패턴
        private const val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,20}$"
    }

}