package com.example.pocketmap.domain.models

data class Place(
    val id: Int = 0,
    val name: String,
    val lat: Double,
    val lon: Double
)
