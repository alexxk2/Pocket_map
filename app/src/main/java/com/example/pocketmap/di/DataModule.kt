package com.example.pocketmap.di

import com.example.pocketmap.data.db.PlacesDatabase
import com.example.pocketmap.data.db.RoomStorage
import com.example.pocketmap.data.db.impl.RoomStorageImpl
import com.example.pocketmap.data.repositories_impl.StorageRepositoryImpl
import com.example.pocketmap.domain.repositories.StorageRepository
import org.koin.dsl.module

val dataModule = module {

    single<RoomStorage> { RoomStorageImpl(placesDatabase = get()) }

    single<StorageRepository> { StorageRepositoryImpl(roomStorage = get()) }

    single<PlacesDatabase> { PlacesDatabase.getDatabase(context = get()) }

}