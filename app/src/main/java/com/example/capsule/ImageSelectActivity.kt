package com.example.capsule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageSelectActivity : AppCompatActivity() {
    val TAG = "ImageSelectActivity"
    private lateinit var mAdapter: GalleryImageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)
        recyclerView = findViewById(R.id.gallery_recyclerView)
        Log.d(TAG, "나 실행됐음ㅎㅎ")
        val mData = intent.getStringArrayListExtra("imageList")
        Log.d(TAG, "$mData")
        mAdapter = GalleryImageAdapter(this, mData)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }
}