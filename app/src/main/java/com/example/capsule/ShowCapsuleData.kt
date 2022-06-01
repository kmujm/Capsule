package com.example.capsule

import android.net.Uri

data class ShowCapsuleData(val title:String, val date:String, val photo: Uri, val regPhoto: MutableList<String>, val content:String)