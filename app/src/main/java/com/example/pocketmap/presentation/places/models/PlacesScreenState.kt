package com.example.pocketmap.presentation.places.models

sealed interface PlacesScreenState{
    object Loading: PlacesScreenState
    object Content: PlacesScreenState
    object Error: PlacesScreenState
    object Empty: PlacesScreenState
}