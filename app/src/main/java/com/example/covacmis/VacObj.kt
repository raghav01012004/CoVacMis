package com.example.covacmis

data class VacObj(
    val Age_group: List<Double>,
    val companies: List<String>,
    val dose_count: Int,
    val dose_gap: List<Int>,
    val gender: String,
    val live_attenuated: Boolean,
    val name: String,
    val type: List<String>
)