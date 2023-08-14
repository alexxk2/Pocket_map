package com.example.pocketmap.domain.storage

import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.repositories.StorageRepository

class GetPlaceUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(id: Int): Place = storageRepository.getPlace(id)
}