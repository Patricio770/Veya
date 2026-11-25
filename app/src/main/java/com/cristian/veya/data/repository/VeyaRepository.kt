package com.cristian.veya.data.repository

import com.cristian.veya.data.remote.RetrofitClient
import com.cristian.veya.data.remote.VeyaApiService
import com.cristian.veya.models.EstadisticaDia
import com.cristian.veya.models.Producto
import com.cristian.veya.models.PuntoVeya

class VeyaRepository(
    private val service: VeyaApiService = RetrofitClient.apiService
) {
    suspend fun listProductos(): List<Producto> = service.getProductos()
    suspend fun listPuntos(): List<PuntoVeya> = service.getPuntos()
    suspend fun listEstadisticas(): List<EstadisticaDia> = service.getEstadisticas()
}