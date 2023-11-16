package com.example.covacmis

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class HospitalLogin(
    val hospitalId: String,
    val hospitalName: String
):Serializable
