package com.example.capsule

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageSelectActivity : AppCompatActivity() {
    val TAG = "ImageSelectActivity"
    private lateinit var mAdapter: GalleryImageAdapter
    private lateinit var recyclerView: RecyclerView
    private val submitButton: AppCompatButton by lazy {
        findViewById(R.id.submitButton)
    }
    var uriArr = arrayListOf("1")
    var ImageList = mutableListOf<ImageData>()
    val passData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)
        recyclerView = findViewById(R.id.gallery_recyclerView)
        Log.d(TAG, "나 실행됐음ㅎㅎ")
        val mData = intent.getStringArrayListExtra("imageList")
        Log.d(TAG, "$mData")
        if (mData != null) {
            uriArr.addAll(mData)
        }
        passData.add(mData.toString())
        initView()
        initSubmitBtn()
        selectImage()

    }

    private fun selectImage() {
        mAdapter.setOnItemClickListener(object : GalleryImageAdapter.onItemClickListener {
            override fun onItemClick(position: Int, holder: ImageView) {
                if (position == 0) {
                    //갤러리로
                } else {
                    if (ImageList[position].selected) {
                        ImageList[position].selected = false
                        holder.clearColorFilter()
                    } else {
                        ImageList[position].selected = true
                        holder.setColorFilter(
                            Color.parseColor("#BDBDBD"),
                            PorterDuff.Mode.MULTIPLY
                        )
                    }

                }
            }

        })
    }

    private fun initSubmitBtn() {
        submitButton.setOnClickListener {
            Log.d(TAG, "완료 버튼 누름!")
            val intent = Intent(this, PostCapsuleActivity::class.java)
            //intent.putStringArrayListExtra("passData",ArrayList(passData))
            startActivity(intent)
            this.finish()
        }
    }

    private fun initView() {
        for (item in uriArr) {
            ImageList.add(ImageData(item, false))
        }
        mAdapter = GalleryImageAdapter(this, ImageList)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

}




