package com.example.covacmis

import android.os.Parcel
import android.os.Parcelable

data class DataClass(var dataVaccineName:String,var ageGroup:String, var doseCount:String): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!,parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dataVaccineName)
        parcel.writeString(ageGroup)
        parcel.writeString(doseCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataClass> {
        override fun createFromParcel(parcel: Parcel): DataClass {
            return DataClass(parcel)
        }

        override fun newArray(size: Int): Array<DataClass?> {
            return arrayOfNulls(size)
        }
    }
}
