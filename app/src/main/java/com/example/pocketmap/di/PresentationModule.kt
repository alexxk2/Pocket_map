package com.example.pocketmap.di

import com.example.pocketmap.presentation.map.view_model.MapViewModel
import com.example.pocketmap.presentation.places.view_model.PlacesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<MapViewModel> { MapViewModel(addNewPlaceUseCase = get()) }

    viewModel<PlacesViewModel> {
        PlacesViewModel(
            getAllPlacesUseCase = get(),
            deleteAllPlacesUseCase = get()
        )
    }
}