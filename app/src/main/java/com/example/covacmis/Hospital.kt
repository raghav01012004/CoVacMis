package com.example.covacmis

data class Hospital(
    val hospitalId:String,
    val fullname: String,
    val city: String,
    val distance: String,
    val latitude:String,
    val longitude:String,
    var isSelected:Boolean = false
//    val password: String,
//    val mobile: String,
//    val username: String,
//    val latitude: Float,
//    val longitude: Float
)
