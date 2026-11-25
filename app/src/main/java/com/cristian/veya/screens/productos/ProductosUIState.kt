package com.cristian.veya.screens.productos

import com.cristian.veya.models.Producto

sealed interface ProductosUIState {
    data object Loading : ProductosUIState
    data class Success(val productos: List<Producto>) : ProductosUIState
    data class Error(val mensaje: String) : ProductosUIState
}