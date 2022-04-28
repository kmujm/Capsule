package com.example.capsule

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.capsule.databinding.ActivitySignupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var isEmailPass = false
    private var isPasswordPass = false
    private var isStateShowingPassword = true
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDetctEmailEditText()
        initCheckPasswordPattern()
        initView()
    }

    private fun initView() {
        // 제출버튼 OFF상태로 초기화
        offSubmitButton()
        clearPasswordEditView()
        initPasswordEyeButton()
        initCheckEmailButton()
        initSignupSubmitButton()

    }


    // 완료버튼 눌렀을때 리스너
    private fun initSignupSubmitButton() {
        binding.signupSubmitButton.setOnClickListener {
            Log.d(TAG, "SUBMIT ON CLICKED!!")
            // 두개가 일치하는지 확인
            if (isSamePassword().not()) {
                showPasswordErrorMessage()
                binding.checkPasswordEditText.background =
                    getDrawable(R.drawable.edittext_alert_background)
                isPasswordPass = false
                return@setOnClickListener
            }

            // 비밀번호 정규식 확인
            if (isPasswordPattern().not()) return@setOnClickListener

            // 다음 두 상태 검사 후 다음 인탠트로 넘겨줌
            if ((isEmailPass && isPasswordPass).not()) return@setOnClickListener
            Log.d(TAG, "닉네임 페이지로 이동!")
            val intent = Intent(this, NicknameActivity::class.java).apply {
                putExtra("userEmail", binding.emailEditText.text.toString())
                putExtra("userPassword", binding.passwordEditText.text.toString())
            }
            startActivity(intent)


        }
    }

    private fun initCheckPasswordPattern() {
        binding.checkPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 다시 원상복귀 해주기
                binding.checkPasswordEditText.background =
                    getDrawable(R.drawable.edittext_background)
                hidePasswordAlertIconAndMessage()

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentPasswordEditText = binding.passwordEditText.text.toString()
                val currentPasswordCheckEditText = binding.checkPasswordEditText.text.toString()
                when {
                    currentPasswordEditText.isNullOrBlank()
                        .not() && currentPasswordCheckEditText.isNullOrBlank()
                        .not() && isEmailPass -> {
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
    }

    private fun initCheckEmailButton() {
        // 이메일 중복확인 버튼 클릭 리스러
        binding.checkEmailButton.setOnClickListener {
            val curEmail = binding.emailEditText.text.toString()

            db.collection("users")
                .whereEqualTo("Email", curEmail)
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        Log.d(TAG, "${doc.id} => ${doc.data}")
                        if (doc.data.get("Email").toString() == curEmail) {
//                            Toast.makeText(this, "사용 불가능한 이메일 입니다", Toast.LENGTH_SHORT).show()
                            // 중복일 시
                            showEmailCheckErrorMessage()
                            showEmailEditTextAlertIcon()
                            offCheckEmailButton()
                            offSubmitButton()
                            isEmailPass = false
                            return@addOnSuccessListener
                        }
                    }
                    // 중복이 아닐 시
//                    Toast.makeText(this, "사용 가능한 이메일 입니다", Toast.LENGTH_SHORT).show()
                    isEmailPass = true
                    showEmailCheckPassMessage()
                    onCheckEamilButton()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    return@addOnFailureListener
                }
        }
    }


    private fun initPasswordEyeButton() {
        // 비밀번호 SHOW ON/OFF 클릭 리스너
        binding.passwordEyeButton.setOnClickListener {
            if (isStateShowingPassword) { // 비밀번호 보이기 모드 -> 안보이기 모드
                binding.passwordEyeButton.apply {
                    isStateShowingPassword = false
                    background = getDrawable(R.drawable.eye_close)
                }
                binding.passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

            } else { // 비밀번호 안보이기 모드 -> 보이기 모드
                binding.passwordEyeButton.apply {
                    isStateShowingPassword = true
                    background = getDrawable(R.drawable.eye_open)
                }
                binding.passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }
        }
    }

    private fun clearPasswordEditView() {
        binding.passwordEditText.setOnClickListener {
            binding.passwordEditText.setText("")
            hidePasswordAlertIconAndMessage()
        }

        binding.checkPasswordEditText.setOnClickListener {
            binding.checkPasswordEditText.setText("")
            hidePasswordAlertIconAndMessage()
        }
    }

    private fun hidePasswordAlertIconAndMessage() {
        binding.passwordEditTextAlertImageView.isVisible = false
        binding.checkPasswordMessageTextView.isVisible = false
    }

    // AlertIcon, 비밀번호 에러메시지, 빨간색 줄 띄우기
    private fun showPasswordErrorMessage() {
        binding.checkPasswordMessageTextView.setText(CHECK_PASSWORD_ERROR_MESSAGE)
        binding.checkPasswordMessageTextView.setTextColor(ContextCompat.getColor(this,
            R.color.cost))
        binding.checkPasswordMessageTextView.isVisible = true

        binding.passwordEditTextAlertImageView.isVisible = true
    }


    private fun isPasswordPattern(): Boolean {
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

    private fun isSamePassword(): Boolean {
        val curPassword = binding.passwordEditText.text.toString()
        val checkPassword = binding.checkPasswordEditText.text.toString()
        if (curPassword == checkPassword) {
            Log.d(TAG, "IS SAME!!")
            return true
        }
        Log.d(TAG, "IS NOT SAME!!")
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
        // 에딧테스트 빨간색 테두리 변경
        binding.emailEditText.background = getDrawable(R.drawable.edittext_alert_background)
    }

    private fun showEmailCheckPassMessage() {
        binding.emailCheckMessageTextView.apply {
            setText(CHECK_EMAIL_PASS_MESSAGE)
            isVisible = true
        }
        binding.emailCheckMessageTextView.setTextColor(ContextCompat.getColor(this,
            R.color.secondary))
    }

    private fun showEmailCheckErrorMessage() {
        binding.emailCheckMessageTextView.apply {
            isVisible = true
            setText(CHECK_EMAIL_ERROR_MESSAGE)
        }
        binding.emailCheckMessageTextView.setTextColor(ContextCompat.getColor(this, R.color.cost))
    }


    // 아메일 정규식 확인을 위한 함수 초기화
    private fun initDetctEmailEditText() {
        // 이메일 정규식 판별을 위한 이벤트 리스너
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isEmailPass = false
                hideEmailPassOrErrorMessage()
                hideEmailEditTextAlertIcon()

                // 배경색 원위치
                binding.emailEditText.background = getDrawable(R.drawable.edittext_background)

                Log.d(TAG, isEmailPass.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val editText = binding.emailEditText.text.toString()
                if (isEmailPattern(email = editText)) {
                    onCheckEamilButton()
                } else {
                    offCheckEmailButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun hideEmailPassOrErrorMessage() {
        binding.emailCheckMessageTextView.isVisible = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun offCheckEmailButton() {
        binding.checkEmailButton.apply {
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
    private fun isEmailPattern(email: String?): Boolean {
        val pattern = android.util.Patterns.EMAIL_ADDRESS
        if (email.isNullOrEmpty()) return false
        when {
            pattern.matcher(email).matches() -> return true
            else -> return false
        }
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