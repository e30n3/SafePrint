package com.involta.safeprint.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PdfRequest(
    @SerialName("token")
    val token: String,

    @SerialName("lastUpdate")
    val lastUpdate: String,
)
