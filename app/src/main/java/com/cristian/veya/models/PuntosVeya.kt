package com.cristian.veya.models

data class PuntoVeya(
    val id: Int,
    val nombre: String,
    val distrito: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
)