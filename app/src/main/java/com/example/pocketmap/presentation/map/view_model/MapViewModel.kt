package com.example.pocketmap.presentation.map.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.storage.AddNewPlaceUseCase
import kotlinx.coroutines.launch

class MapViewModel(
    private val addNewPlaceUseCase: AddNewPlaceUseCase
): ViewModel() {



    fun addNewPlace(place: Place){
        viewModelScope.launch {
            addNewPlaceUseCase.execute(place)
        }
    }
}