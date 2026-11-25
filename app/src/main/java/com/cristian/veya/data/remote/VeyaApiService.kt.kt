package com.cristian.veya.data.remote

import com.cristian.veya.models.EstadisticaDia
import com.cristian.veya.models.Producto
import com.cristian.veya.models.PuntoVeya
import retrofit2.http.GET

interface VeyaApiService {
    @GET("productos.php")
    suspend fun getProductos(): List<Producto>

    @GET("puntos.php")
    suspend fun getPuntos(): List<PuntoVeya>

    @GET("estadisticas.php")
    suspend fun getEstadisticas(): List<EstadisticaDia>
}