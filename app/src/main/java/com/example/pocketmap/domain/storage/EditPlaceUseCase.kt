package com.example.pocketmap.domain.storage

import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.repositories.StorageRepository

class EditPlaceUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(place: Place) = storageRepository.editPlace(place)
}