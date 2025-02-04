package org.example.project.data.models.forcastmodel

import kotlinx.serialization.Serializable
import org.example.project.data.models.model.Coord

@Serializable
data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)