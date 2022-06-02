package com.example.capsule

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetectedCatagoryActivity : AppCompatActivity(){

    private lateinit var cAdapter : CatagoryAdapter
    private val catagoryList = mutableListOf<CatagoryItem>()

    private val korTitleList = mutableListOf<String>(KOR_FASHION, KOR_FOOD, KOR_LIVING, KOR_PLACE,
        KOR_PLANT)
    private val engTitleList = mutableListOf<String>(ENG_FASHION, ENG_FOOD, ENG_LIVING, ENG_PLACE, ENG_PLANT)

    // 인탠트에서 받아오는 리스트
    private val list = mutableListOf<Int>(1,0,0,0,0)

    private val cRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.dcCatagoryRecyclerView)
    }

    private val retryTextButton by lazy {
        findViewById<TextView>(R.id.dcRetryTextButton)
    }

    private val backButton by lazy {
        findViewById<Button>(R.id.dcBackButton)
    }

    private val detectedImageView by lazy {
        findViewById<ImageView>(R.id.picture)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objectdetection)

        // 데이터를 받았을때 리팩토링 해야함

        refactoryingIntentData(list)
        initCatagoryRecyclerView()
        initRetryTextButton()
        initBackButton()
        initDetectedImageView()


    }

    private fun initDetectedImageView() {
        // TODO 인탠트로 넘어온 이미지 뷰 셋하기
    }

    private fun initBackButton() {
        backButton.setOnClickListener {
            finish()
        }
    }

    // 어댑터에 넘겨줄 리스트를, 형식에 맞게 전환
    private fun refactoryingIntentData(list : MutableList<Int>){

        for ( i in 0..4 ) {
            val item = CatagoryItem(korTitleList[i],engTitleList[i],list[i])
            catagoryList.add(item)
        }
    }

    private fun initCatagoryRecyclerView() {
        cRecyclerView.layoutManager = LinearLayoutManager(this)
        // 선택된 카테고리 넘겨줌
        cAdapter = CatagoryAdapter { selectedCatagory ->
            val selectedGalleryActivity = Intent(this,MainActivity::class.java)
            selectedGalleryActivity.putExtra(INTENT_KEY_SELECTED_CATAGORY,selectedCatagory)
            startActivity(selectedGalleryActivity)
        }
        cRecyclerView.adapter = cAdapter
        cAdapter.submitList(catagoryList)
    }


    private fun initRetryTextButton() {
        retryTextButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        // 인텐트 키
        const val INTENT_KEY_SELECTED_CATAGORY = "selectedCatagory"

        //  카테고리 이름
        const val KOR_FASHION = "패션"
        const val KOR_FOOD = "음식"
        const val KOR_LIVING = "리빙"
        const val KOR_PLACE = "장소"
        const val KOR_PLANT = "식물"
        const val ENG_FASHION = "Fashion goods"
        const val ENG_FOOD = "Foods"
        const val ENG_LIVING = "Living"
        const val ENG_PLACE = "Place"
        const val ENG_PLANT = "Plants"
    }
}