package com.example.mynote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note (
    var id: Int = 0,
    var title: String = "",
    var desc: String = "",
    var time: String = ""
        ) : Parcelable