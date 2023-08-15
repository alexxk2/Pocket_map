package com.example.pocketmap.domain.storage

import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.repositories.StorageRepository

class GetAllPlacesUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(): List<Place> = storageRepository.getAllPlaces()
}