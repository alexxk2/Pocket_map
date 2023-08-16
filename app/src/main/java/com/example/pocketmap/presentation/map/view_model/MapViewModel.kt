package com.example.pocketmap.presentation.map.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.storage.AddNewPlaceUseCase
import com.example.pocketmap.domain.storage.GetAllPlacesUseCase
import com.example.pocketmap.presentation.map.models.MapScreenState
import com.example.pocketmap.presentation.places.models.PlacesScreenState
import kotlinx.coroutines.launch

class MapViewModel(
    private val addNewPlaceUseCase: AddNewPlaceUseCase,
    private val getAllPlacesUseCase: GetAllPlacesUseCase
): ViewModel() {

    private val _screenState = MutableLiveData<MapScreenState>()
    val screenState: LiveData<MapScreenState> = _screenState

    private val _listOfPlaces = MutableLiveData<List<Place>>()
    val listOfPlaces: LiveData<List<Place>> = _listOfPlaces


    fun addNewPlace(place: Place){
        viewModelScope.launch {
            addNewPlaceUseCase.execute(place)
        }
    }

    fun getAllPlaces(){
        _screenState.value = MapScreenState.Loading

        viewModelScope.launch {

            try {
                _listOfPlaces.value = getAllPlacesUseCase.execute()
                _screenState.value = MapScreenState.Content

            } catch (e: Exception) {
                _screenState.value = MapScreenState.Error
            }
        }

    }
}