package com.example.weatherapp.model

import java.io.Serializable

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
): Serializable