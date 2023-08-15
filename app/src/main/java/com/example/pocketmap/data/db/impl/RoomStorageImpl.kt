package com.example.pocketmap.data.db.impl

import com.example.pocketmap.data.db.PlacesDatabase
import com.example.pocketmap.data.db.RoomStorage
import com.example.pocketmap.data.db.dto.PlaceDto

class RoomStorageImpl(private val placesDatabase: PlacesDatabase) : RoomStorage {

    private val placesDao = placesDatabase.placesDao()

    override suspend fun addNewPlace(placeDto: PlaceDto) = placesDao.addNewItem(placeDto)

    override suspend fun deleteAllPlaces() = placesDao.deleteAllItems()

    override suspend fun deletePlace(placeDto: PlaceDto) = placesDao.deleteItem(placeDto)

    override suspend fun editPlace(placeDto: PlaceDto) = placesDao.editItem(placeDto)

    override suspend fun getAllPlaces(): List<PlaceDto> = placesDao.getAllItems()

    override suspend fun getPlace(id: Int): PlaceDto = placesDao.getItem(id)
}