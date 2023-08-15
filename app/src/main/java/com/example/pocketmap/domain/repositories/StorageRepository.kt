package com.example.pocketmap.domain.repositories

import com.example.pocketmap.domain.models.Place

interface StorageRepository {

    suspend fun addNewPlace(place: Place)
    suspend fun deleteAllPlaces()
    suspend fun deletePlace(place: Place)
    suspend fun editPlace(place: Place)
    suspend fun getAllPlaces(): List<Place>
    suspend fun getPlace(id: Int): Place

}