package com.example.capsule

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageSelectActivity : AppCompatActivity() {
    val imageUriList = mutableSetOf<String>()

    val TAG = "ImageSelectActivity"

    private lateinit var mAdapter: GalleryImageAdapter
    private lateinit var recyclerView: RecyclerView
    private val backButton: Button by lazy {
        findViewById(R.id.backBtn)
    }
    private val submitButton: AppCompatButton by lazy {
        findViewById(R.id.submitButton)
    }
    var uriArr = arrayListOf("1")
    var ImageList = mutableListOf<ImageData>()
    val passData = mutableSetOf<String>()

    private val mainImageUri : Uri by lazy{
        intent.getParcelableExtra("mainImage")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)
        recyclerView = findViewById(R.id.gallery_recyclerView)
        val mData = intent.getStringArrayListExtra("imageList")
        Log.d(TAG, "$mData")
        if (mData != null) {
            uriArr.addAll(mData)
        }
        initBackBtn()
        initView()
        initSubmitBtn()
        selectImage()

    }

    private fun selectImage() {
        mAdapter.setOnItemClickListener(object : GalleryImageAdapter.onItemClickListener {
            override fun onItemClick(position: Int, holder: ImageView, check: ImageView) {
                if (position == 0) {
                    openGallery()
                } else {
                    if (ImageList[position].selected) {
                        ImageList[position].selected = false
                        holder.clearColorFilter()
                        check.visibility = ImageView.GONE
                    } else {
                        ImageList[position].selected = true
                        holder.setColorFilter(
                            Color.parseColor("#BDBDBD"),
                            PorterDuff.Mode.MULTIPLY
                        )
                        check.visibility = ImageView.VISIBLE
                    }

                }
            }

        })
    }

    private fun openGallery() {
        val intent = Intent(this, SelectPicActivity::class.java)
        startActivity(intent)
    }

    private fun initBackBtn() {
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun initSubmitBtn() {
        submitButton.setOnClickListener {
            for (item in ImageList) {
                if (item.selected) {
                    passData.add(item.contentUri)
                }
            }
            Log.d(TAG, "갤러리 테스트 $imageUriList")
            Log.d(TAG, "넘겨주는 값 $passData")
            val intent = Intent(this, PostCapsuleActivity::class.java).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putStringArrayListExtra("passData", ArrayList(passData))
            intent.putExtra("mainImage", mainImageUri.toString())
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

