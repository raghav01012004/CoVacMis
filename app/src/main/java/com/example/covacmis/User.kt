package com.example.covacmis

import java.io.Serializable

data class User(
    val fullname: String,
    val username: String,
    val dob: String,
    val password: String,
    val gender: String,
    val mobile_no: String,
    val vaccines: Map<String,Any>,
): Serializable