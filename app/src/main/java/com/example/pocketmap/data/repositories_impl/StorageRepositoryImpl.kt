package com.example.pocketmap.data.repositories_impl

import com.example.pocketmap.data.db.RoomStorage
import com.example.pocketmap.data.db.dto.PlaceDto
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.repositories.StorageRepository

class StorageRepositoryImpl(private val roomStorage: RoomStorage): StorageRepository {


    override suspend fun addNewPlace(place: Place) {
        val mappedPlace = mapPlaceToData(place)
        roomStorage.addNewPlace(placeDto = mappedPlace)
    }

    override suspend fun deleteAllPlaces() = roomStorage.deleteAllPlaces()

    override suspend fun deletePlace(place: Place) {
        val mappedPlace = mapPlaceToData(place)
        roomStorage.deletePlace(placeDto = mappedPlace)
    }

    override suspend fun editPlace(place: Place) {
        val mappedPlace = mapPlaceToData(place)
        roomStorage.editPlace(placeDto = mappedPlace)
    }

    override suspend fun getAllPlaces(): List<Place> {
        val resultFormData = roomStorage.getAllPlaces()
        return resultFormData.map{placeDto->
            mapPlaceToDomain(placeDto)
        }
    }

    override suspend fun getPlace(id: Int): Place {
        val resultFromData = roomStorage.getPlace(id)
        return mapPlaceToDomain(placeDto = resultFromData)
    }

    private fun mapPlaceToData(place: Place): PlaceDto{
        with(place){
            return PlaceDto(
                id = id,
                name = name,
                lat = lat,
                lon = lon
            )
        }
    }

    private fun mapPlaceToDomain(placeDto: PlaceDto): Place{
        with(placeDto){
            return Place(
                id = id,
                name = name,
                lat = lat,
                lon = lon
            )
        }
    }
}