package com.example.covacmis

interface LocationFetchListener {
    fun onLocationFetchCompleted(latitude: String, longitude: String, address: String)
}
