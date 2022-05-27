package com.example.capsule

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostCapsuleActivity : AppCompatActivity() {


    private lateinit var iAdapter: ImageAdpater

    private val imageRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.pcImageRecyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_capsule)

        // TODO 내가 넘겨받았다고 가정하는 데이터
        // 메인이미지uri와, 서브 이미지uri, -> 뭐가 메인이고 뭐가 서브인지 구분되게 들어와야함
        // 선택한 카테고리



        // TODO 현재 날짜 파싱해서 텍스트뷰에 바인딩

        // TODO 글자수 제한두기 및 현재 글자수 파악하기


        initRecyclerView()


        // TODO 저장 버튼 눌렀을때 해야 할 것 -> IMAGE 푸시해서 url 받아오기 성공시,
        // 이미지 파이어베이스 스토리지에 올리고 이미지url받아오기
        // 캡슐을 알맞게 리얼타임DB에 푸시 시켜야함
        // TODO 디비에 보낼 데이터 (미완)
        // 카테고리 ,제목, 현재날짜, 사진URL(MAIN사진과 SUB사진 구분), 메모한 글




    }

    private fun initRecyclerView() {
        imageRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        iAdapter = ImageAdpater()
        imageRecyclerView.adapter = iAdapter

        val dummyData = mutableListOf<ImageItem>(ImageItem("1", true),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false),
            ImageItem("1", false))
        iAdapter.submitList(dummyData)
    }


}