package com.example.capsule

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shivampip.LinedEditText
import java.text.SimpleDateFormat
import java.util.*

class PostCapsuleActivity : AppCompatActivity() {


    private lateinit var iAdapter: ImageAdpater
    private lateinit var nowTime: String
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val imageRecyclerView by lazy { findViewById<RecyclerView>(R.id.pcImageRecyclerView) }
    private val nowTimeTextView by lazy { findViewById<TextView>(R.id.pcPostedDateTextView) }
    private val editText by lazy { findViewById<LinedEditText>(R.id.pcPostEditText) }
    private val titleEditText by lazy { findViewById<EditText>(R.id.pcTitleEditText) }
    private val countTextView by lazy { findViewById<TextView>(R.id.pcCountTextTextView) }
    private val submitButton by lazy { findViewById<AppCompatButton>(R.id.pcSubmitAppCompatButton) }


    private val LOG = "PostCapsuleActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_capsule)
        // 정민 데이터 전달 확인 해본 코드
        val mData = intent.getStringArrayListExtra("passData")
        Log.d("받아온 값", mData.toString())


        // TODO 내가 넘겨받았다고 가정하는 데이터
        // 메인이미지uri와, 서브 이미지uri, -> 뭐가 메인이고 뭐가 서브인지 구분되게 들어와야함
        // 선택한 카테고리

        // db and auth init
        // TODO auth 널체크후, db 레퍼런스 잡아주기
        db = Firebase.database.reference.child("Users").child(GET_AUTH_ID)
        auth = Firebase.auth


        initAndCountText()
        initAndSetNowTime()
        initRecyclerView()

        // TODO 저장 버튼 눌렀을때 해야 할 것 -> IMAGE 푸시해서 url 받아오기 성공시
        initSubmitButton()


    }

    private fun initAndCountText() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.d(LOG, "afterTextChanged ${s?.let { it.length }}")
                if (s.isNullOrBlank()) {
                    countTextView.text = "0/250"
                } else {
                    countTextView.text = "${s.length}/250"
                }
            }
        })
    }

    private fun initAndSetNowTime() {
        // TODO 현재 날짜 파싱해서 텍스트뷰에 바인딩
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh시 mm분")
        nowTime = dateFormat.format(date)
        nowTimeTextView.text = nowTime
    }

    private fun initRecyclerView() {
        imageRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        iAdapter = ImageAdpater()
        imageRecyclerView.adapter = iAdapter

        val dummyData = mutableListOf<ImageItem>(
            ImageItem("1", true),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false)
        )
        iAdapter.submitList(dummyData)
    }

    private fun initSubmitButton() {
        submitButton.setOnClickListener {
            // 스토리지에 이미지 업로드
            // 업로드한 이미지를 받아옴
            // 이미지를 정상적으로 받아왔으면 리얼타임 데이터베이스에, 데이터들 업로드
            Log.d(LOG, "submitBtn Clicked!!")
            // 제목 null 체크
//            if(auth.uid == null ) {
//                // 로그인 상태가 아니기 때문에 에러 발생, 로그인 페이지로 돌림
//                Toast.makeText(this,"로그인 정보가 올바르지 않습니다.",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this,LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }

            if (titleEditText.text.isNullOrBlank()) {
                Toast.makeText(this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                uploadImageToStorage(
                    mSuccessHandler = { uploadToRealTimeDB() },
                    mErrorHandler = {
                        Toast.makeText(this, "사진 저장에 실패 했습니다", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }

    private fun uploadImageToStorage(mSuccessHandler: () -> Unit, mErrorHandler: () -> Unit) {
        Log.d(LOG, "uploadImageToStorage called !!")
        // 여기서 이미지 업로드에 성공하면, 컴플리트 리스너를 달아주고 그 안에서 성공 콜백함수 실행 !
        mSuccessHandler()

    }

    private fun uploadToRealTimeDB() {
        // TODO 업로드시  메인이미지, 서브이미지, 타이틀 null 체크 합시다 !
        // categoty , content, date, detectImage(메인), registerImage(서브), title
        Log.d(LOG, "uploadToDB Called!!")
        // push로 키값을 한번에 주기 때문에, 데이터들을 묶어서 업로드 해야하기 때문에 익명객체 생성 후 업로드
        val forUploadObject = object {
            val content = editText.text.toString()
            val title = titleEditText.text.toString()
            val date = nowTimeTextView.text.toString()

            // 인탠트로 받아온 인식된 category 넣어주기
            val category = "Fashion goods"

            // 인탠트로 받아온 이미지 url 넣어주기
            val detectImage = "main-image-URL"
            val registerImage = mutableListOf<String>("url123", "url1234", "url1234")
        }
        db.push().setValue(forUploadObject)

    }

    // 현재 포커스된 뷰의 영역이 아닌 다른 곳을 클릭 시 키보드를 내리고 포커스 해제
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
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
        const val GET_AUTH_ID = "abcd123123"
    }


}