package com.example.pocketmap.domain.storage

import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.repositories.StorageRepository

class DeletePlaceUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(place: Place) = storageRepository.deletePlace(place)
}