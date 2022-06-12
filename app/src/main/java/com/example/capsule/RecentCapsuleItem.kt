package com.example.capsule

import android.net.Uri
import java.util.*

data class RecentCapsuleItem (
    val capsuleDate : String,
    val capsuleTitle : String,
    val pictureList : MutableList<Uri>,
    val capsuleKey : String
    )