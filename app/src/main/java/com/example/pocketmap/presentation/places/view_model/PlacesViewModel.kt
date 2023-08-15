package com.example.pocketmap.presentation.places.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.domain.storage.DeleteAllPlacesUseCase
import com.example.pocketmap.domain.storage.DeletePlaceUseCase
import com.example.pocketmap.domain.storage.GetAllPlacesUseCase
import com.example.pocketmap.presentation.places.models.ScreenState
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val deleteAllPlacesUseCase: DeleteAllPlacesUseCase
): ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _listOfPlaces = MutableLiveData<List<Place>>()
    val listOfPlaces: LiveData<List<Place>> = _listOfPlaces


    fun getAllPlaces(){
        _screenState.value = ScreenState.Loading

        viewModelScope.launch {

            try {
                _listOfPlaces.value = getAllPlacesUseCase.execute()
                _screenState.value =
                    if (listOfPlaces.value?.isEmpty() == true) ScreenState.Empty else ScreenState.Content

            } catch (e: Exception) {
                _screenState.value = ScreenState.Error
            }
        }

    }

    fun deleteAllPlaces(){
        _screenState.value = ScreenState.Loading

        viewModelScope.launch {
            deleteAllPlacesUseCase.execute()
            _screenState.value = ScreenState.Empty
        }

    }


}