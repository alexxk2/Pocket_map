package com.example.pocketmap.domain.storage

import com.example.pocketmap.domain.repositories.StorageRepository

class DeleteAllPlacesUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute() = storageRepository.deleteAllPlaces()
}