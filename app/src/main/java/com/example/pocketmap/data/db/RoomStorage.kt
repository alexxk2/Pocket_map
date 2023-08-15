package com.example.pocketmap.data.db

import com.example.pocketmap.data.db.dto.PlaceDto
import com.example.pocketmap.domain.models.Place

interface RoomStorage {

    suspend fun addNewPlace(placeDto: PlaceDto)
    suspend fun deleteAllPlaces()
    suspend fun deletePlace(placeDto: PlaceDto)
    suspend fun editPlace(placeDto: PlaceDto)
    suspend fun getAllPlaces(): List<PlaceDto>
    suspend fun getPlace(id: Int): PlaceDto
}