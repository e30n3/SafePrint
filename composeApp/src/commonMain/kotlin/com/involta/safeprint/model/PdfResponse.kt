package com.involta.safeprint.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PdfResponse(
    @SerialName("data")
    val _data: String? = null,

    @SerialName("meta")
    val _meta: PdfResponseMeta? = null
) {
    private val code = _meta?.code ?: -1L
    val pdfBytes = Base64.getDecoder().decode(_data) ?: byteArrayOf()
    val isSuccess = _meta?.isSuccess ?: false
    val message = "$code: ${_meta?.message ?: "Произошла неизвестная ошибка на сервере"}"
}

@Serializable
data class PdfResponseMeta(
    @SerialName("success")
    val isSuccess: Boolean? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("code")
    val code: Long? = null,
)