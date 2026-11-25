package com.cristian.veya.screens.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristian.veya.data.repository.VeyaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PuntosViewModel(
    private val repository: VeyaRepository = VeyaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PuntosUIState>(PuntosUIState.Loading)
    val uiState: StateFlow<PuntosUIState> = _uiState.asStateFlow()

    fun fetchPuntos() {
        viewModelScope.launch {
            _uiState.value = PuntosUIState.Loading
            _uiState.value = try {
                PuntosUIState.Success(repository.listPuntos())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                PuntosUIState.Error("No se pudieron cargar las ubicaciones: ${e.message}")
            }
        }
    }

    init {
        fetchPuntos()
    }
}