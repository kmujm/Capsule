package com.example.capsule

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.jar.Manifest

class ImageSelectActivity : AppCompatActivity() {
    val TAG = "ImageSelectActivity"
    val REQUEST_CODE = 200
    private val Read_Permission = 101

    private lateinit var mAdapter: GalleryImageAdapter
    private lateinit var recyclerView: RecyclerView
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
        Log.d(TAG, "나 실행됐음ㅎㅎ")
        val mData = intent.getStringArrayListExtra("imageList")
        Log.d(TAG, "$mData")
        if (mData != null) {
            uriArr.addAll(mData)
        }
        initView()
        initSubmitBtn()
        selectImage()

    }

    private fun selectImage() {
        mAdapter.setOnItemClickListener(object : GalleryImageAdapter.onItemClickListener {
            override fun onItemClick(position: Int, holder: ImageView) {
                if (position == 0) {
                    openGalleryForImages()
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
            for (item in ImageList) {
                if (item.selected) {
                    passData.add(item.contentUri)
                }
            }
            Log.d(TAG, "넘겨주는 값 $passData")
            val intent = Intent(this, PostCapsuleActivity::class.java).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putStringArrayListExtra("passData", ArrayList(passData))
            intent.putExtra("mainImage", mainImageUri)
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

    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val galleryImgSelectList = mutableSetOf<String>()

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount

                for (i in 0..count!! - 1) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    galleryImgSelectList.add(imageUri.toString())
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
        }
    }

}




