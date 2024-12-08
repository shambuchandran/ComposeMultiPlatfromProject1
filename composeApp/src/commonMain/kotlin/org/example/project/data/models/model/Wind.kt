package org.example.project.data.models.model

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val deg: Int?,
    val gust: Double? =null,
    val speed: Double?
)