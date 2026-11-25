package com.cristian.veya.screens.mapa

import com.cristian.veya.models.PuntoVeya

sealed interface PuntosUIState {
    data object Loading : PuntosUIState
    data class Success(val puntos: List<PuntoVeya>) : PuntosUIState
    data class Error(val mensaje: String) : PuntosUIState
}