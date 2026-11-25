package com.cristian.veya.screens.estadisticas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristian.veya.data.repository.VeyaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstadisticasViewModel(
    private val repository: VeyaRepository = VeyaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<EstadisticasUIState>(EstadisticasUIState.Loading)
    val uiState: StateFlow<EstadisticasUIState> = _uiState.asStateFlow()

    fun fetchEstadisticas() {
        viewModelScope.launch {
            _uiState.value = EstadisticasUIState.Loading
            _uiState.value = try {
                EstadisticasUIState.Success(dias = repository.listEstadisticas())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                EstadisticasUIState.Error("Error al cargar estadísticas: ${e.message}")
            }
        }
    }

    fun llenarBotella() {
        val current = _uiState.value
        if (current is EstadisticasUIState.Success) {
            val nuevoNivel = (current.nivelBotellaMl + 250).coerceAtMost(current.capacidadMaxMl)
            _uiState.value = current.copy(nivelBotellaMl = nuevoNivel)
        }
    }

    fun tomarAgua() {
        val current = _uiState.value
        if (current is EstadisticasUIState.Success && current.nivelBotellaMl > 0) {
            val sorbo = minOf(current.nivelBotellaMl, 250)
            val nuevoNivel = current.nivelBotellaMl - sorbo
            val diasActualizados = current.dias.mapIndexed { index, item ->
                if (index == current.dias.lastIndex) {
                    item.copy(mililitros = item.mililitros + sorbo)
                } else {
                    item
                }
            }
            _uiState.value = current.copy(
                dias = diasActualizados,
                nivelBotellaMl = nuevoNivel
            )
        }
    }

    init {
        fetchEstadisticas()
    }
}