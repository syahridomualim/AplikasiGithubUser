package com.mualim.aplikasigithubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userName: String = "",
    var name: String = "",
    var location: String = "-",
    var repository: Int? = 0,
    var avatar: String? = "",
    var company: String? = "-",
    var followers: Int? = null,
    var following: Int? = null,
    var type: String? =""
):Parcelable