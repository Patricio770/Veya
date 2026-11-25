package com.cristian.veya.screens.estadisticas

import com.cristian.veya.models.EstadisticaDia

sealed interface EstadisticasUIState {
    data object Loading : EstadisticasUIState
    data class Success(
        val dias: List<EstadisticaDia>,
        val nivelBotellaMl: Int = 750,
        val capacidadMaxMl: Int = 1000
    ) : EstadisticasUIState
    data class Error(val mensaje: String) : EstadisticasUIState
}