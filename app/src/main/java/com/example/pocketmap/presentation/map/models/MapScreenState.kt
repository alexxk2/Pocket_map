package com.example.pocketmap.presentation.map.models

sealed interface MapScreenState{
    object Loading: MapScreenState
    object Content: MapScreenState
    object Error: MapScreenState
}