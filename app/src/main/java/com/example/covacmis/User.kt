package com.example.covacmis

data class User(
    val fullname: String,
    val username: String,
    val dob: String,
    val password: String,
    val gender: String,
    val mobile_no: String,
    val vaccines:MutableList<String>,
)