package com.example.pocketmap.presentation.places.view_model

import androidx.lifecycle.ViewModel
import com.example.pocketmap.domain.storage.DeleteAllPlacesUseCase
import com.example.pocketmap.domain.storage.DeletePlaceUseCase
import com.example.pocketmap.domain.storage.GetAllPlacesUseCase

class PlacesViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val deleteAllPlacesUseCase: DeleteAllPlacesUseCase
): ViewModel() {


}