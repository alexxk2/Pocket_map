package com.example.pocketmap.presentation.places.models

sealed interface ScreenState{
    object Loading: ScreenState
    object Content: ScreenState
    object Error: ScreenState
    object Empty: ScreenState
}