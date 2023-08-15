package com.example.pocketmap.di

import com.example.pocketmap.domain.storage.AddNewPlaceUseCase
import com.example.pocketmap.domain.storage.DeleteAllPlacesUseCase
import com.example.pocketmap.domain.storage.DeletePlaceUseCase
import com.example.pocketmap.domain.storage.EditPlaceUseCase
import com.example.pocketmap.domain.storage.GetAllPlacesUseCase
import com.example.pocketmap.domain.storage.GetPlaceUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<AddNewPlaceUseCase> { AddNewPlaceUseCase(storageRepository = get()) }

    factory<DeleteAllPlacesUseCase> { DeleteAllPlacesUseCase(storageRepository = get()) }

    factory<DeletePlaceUseCase> { DeletePlaceUseCase(storageRepository = get()) }

    factory<EditPlaceUseCase> { EditPlaceUseCase(storageRepository = get()) }

    factory<GetAllPlacesUseCase> { GetAllPlacesUseCase(storageRepository = get()) }

    factory<GetPlaceUseCase> { GetPlaceUseCase(storageRepository = get()) }
}