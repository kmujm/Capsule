package com.example.capsule

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ObjectDetectionActivity : AppCompatActivity() {

    private lateinit var cAdapter : CatagoryAdapter
    private val catagoryList = mutableListOf<CatagoryItem>()

    private val korTitleList = mutableListOf<String>(KOR_FASHION,KOR_FOOD,KOR_LIVING,KOR_PLACE,KOR_PLANT)
    private val engTitleList = mutableListOf<String>(ENG_FASHION,ENG_FOOD,ENG_LIVING,ENG_PLACE,ENG_PLANT)


    private var categoryList = mutableListOf(0 /* 패션 */, 0 /* 음식 */, 0 /* 리빙 */, 0 /* 장소 */, 0 /* 식물 */)

    private var mCurrentPhotoPath = ""
    private lateinit var mCurrentPhotoUri : Uri

    private var isNew = false

    private val backButton by lazy {
        findViewById<Button>(R.id.dcBackButton)
    }

    private val image : InputImage by lazy{
        InputImage.fromFilePath(this, mCurrentPhotoUri)
    }

    private val picture : ImageView by lazy{
        findViewById(R.id.picture)
    }

    private val graphicOverlay : GraphicOverlay by lazy{
        findViewById(R.id.graphic_overlay)
    }

    private val cRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.dcCatagoryRecyclerView)
    }

    private val retryTextButton by lazy {
        findViewById<TextView>(R.id.dcRetryTextButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objectdetection)

        isNew = true

        // 데이터를 받았을때 리팩토링 해야함
        initRetryTextButton()
        initBackButton()
    }

    override fun onResume() {
        super.onResume()
        if(isNew){ // 처음 시작할 경우 카메라 촬영 화면으로 전환
            isNew = false
            checkPermission()
            captureCamera()
        } else{
            changePicture(mCurrentPhotoPath)
        }
    }

    private fun captureCamera(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile : File? = null
        try{
            photoFile = createImageFile()
        } catch (ex: IOException){
            Log.e("captureCamera Error", ex.toString())
            return
        }
        if(photoFile != null){
            val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    @Throws(IOException::class)
    fun createImageFile() : File?{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile : File? = null
        val storageDir = File(
            Environment.getExternalStorageDirectory().toString() + "/Pictures",
            "Capsule"
        )
        if(!storageDir.exists()){
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                Log.i("REQUEST_TAKE_PHOTO", "${Activity.RESULT_OK}" + " " + "$resultCode")
                if (resultCode == RESULT_OK) {
                    try {
                        galleryAddPic()
                    } catch (e: Exception) {
                        Log.e("REQUEST_TAKE_PHOTO", e.toString())
                    }

                } else {
                    Toast.makeText(this@ObjectDetectionActivity, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun galleryAddPic() {
        Log.i("galleryAddPic", "Call")
        val mediaScanIntent: Intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val f: File = File(mCurrentPhotoPath)
        mCurrentPhotoUri = Uri.fromFile(f)
        mediaScanIntent.data = mCurrentPhotoUri
        sendBroadcast(mediaScanIntent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                    .setNeutralButton("설정", object: DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:$packageName")
                            startActivity(intent)
                        }
                    })
                    .setPositiveButton("확인", object:DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            finish()
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show()
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("카메라 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                    .setNeutralButton("설정", object: DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.setData(Uri.parse("package:" + getPackageName()))
                            startActivity(intent)
                        }
                    })
                    .setPositiveButton("확인", object:DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface:DialogInterface, i:Int) {
                            finish()
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), MY_PERMISSION_STORAGE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode:Int, @NonNull permissions:Array<String>, @NonNull grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_STORAGE -> for (i in grantResults.indices)
            {
                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                if (grantResults[i] < 0)
                {
                    Toast.makeText(this@ObjectDetectionActivity, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }

    private fun changePicture(filePath : String){
        Log.d("change picture", "change picture")
        val imgFile = File(filePath)
        if (imgFile.exists()) {
            // val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            //picture.setImageBitmap(myBitmap)
            Glide.with(this).load(mCurrentPhotoUri).into(picture)
            initDetection(filePath)
        }
    }

    private fun initDetection(filePath: String){
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val resizedBitmap = BitmapFactory.decodeFile(File(filePath).absolutePath)

        graphicOverlay!!.clear()
        graphicOverlay!!.setImageSourceInfo(
            resizedBitmap.width, resizedBitmap.height, false
        )

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                categoryList = mutableListOf(0 /* 패션 */, 0 /* 음식 */, 0 /* 리빙 */, 0 /* 장소 */, 0 /* 식물 */) // 결과값 리셋
                for (detectedObject in detectedObjects) {
                    if(detectedObject.labels.isNotEmpty()){
                        //인식된 물체에 카테고리가 부여된 경우에만 graphicOverlay에 표시
                        graphicOverlay.add(ObjectGraphic(graphicOverlay, detectedObject))
                        Log.d("object Detected!!", detectedObject.labels[0].text)
                        // 물체 인식 결과값을 넘겨주는 코드
                        when(detectedObject.labels[0].text){
                            "Fashion good"-> categoryList[0]++
                            "Food" -> categoryList[1]++
                            "Home good" -> categoryList[2]++
                            "place" -> categoryList[3]++
                            "plant" -> categoryList[4]++
                        }
                    }
                }
                Log.d("object detection result", categoryList.toString())
                graphicOverlay.postInvalidate()
                refactoryingIntentData(categoryList)
                initCatagoryRecyclerView()
            }
            .addOnFailureListener { e ->
                Log.d("fail", "fail to process Image")
            }
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
            val selectedGalleryActivity = Intent(this,GetDetectedImageActivity::class.java).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedGalleryActivity.putExtra(INTENT_KEY_SELECTED_CATAGORY,selectedCatagory)
            selectedGalleryActivity.putExtra(INTENT_KEY_MAIN_IMAGE, mCurrentPhotoUri) // 사용자가 찍은 사진의 uri를 넘겨줌
            startActivity(selectedGalleryActivity)
        }
        cRecyclerView.adapter = cAdapter
        cAdapter.submitList(catagoryList)
    }

    private fun initRetryTextButton() {
        retryTextButton.setOnClickListener {
            finish()
        }
    }

    companion object{
        var REQUEST_TAKE_PHOTO = 1000
        var MY_PERMISSION_STORAGE = 2000

        // 인텐트 키
        const val INTENT_KEY_SELECTED_CATAGORY = "selectedCatagory"
        const val INTENT_KEY_MAIN_IMAGE = "mainImage"

        //  카테고리 이름
        const val KOR_FASHION = "패션"
        const val KOR_FOOD = "음식"
        const val KOR_LIVING = "리빙"
        const val KOR_PLACE = "장소"
        const val KOR_PLANT = "식물"
        const val ENG_FASHION = "Fashion good"
        const val ENG_FOOD = "Food"
        const val ENG_LIVING = "Home good"
        const val ENG_PLACE = "Place"
        const val ENG_PLANT = "Plant"
    }
}