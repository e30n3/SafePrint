package com.involta.safeprint.screen

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.involta.safeprint.extention.ViewModel
import com.involta.safeprint.model.PdfRequest
import com.involta.safeprint.model.PdfResponse
import com.involta.safeprint.versionDate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.printing.PDFPageable
import java.awt.print.PrinterJob

class MainViewModel(private val viewModelScope: CoroutineScope) : ViewModel(viewModelScope) {

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<Throwable?>(null)
        private set

    val isErrorVisible by derivedStateOf { error != null }
    val isTokenFieldEnabled by derivedStateOf { !isLoading }
    val isPasteButtonEnabled by derivedStateOf { !isLoading }

    var token by mutableStateOf("")
        private set

    fun onTokenChanged(new: String) {
        token = new
        if (new.length == 128) downloadPdfAndPrint(token)
    }

    private val client = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        defaultRequest {
            header("Content-Type", "application/json; charset=UTF-8")
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 30000
            requestTimeoutMillis = 30000
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
    private var exitApplication: () -> Unit = {}
    fun onComposed(clipBoardText: String, exitApplication: () -> Unit) {
        autoCheck(clipBoardText)
        this.exitApplication = exitApplication
    }

    private fun autoCheck(clipBoardText: String) {
        if (clipBoardText.length == 128) onTokenChanged(clipBoardText)
    }

    private fun downloadPdfAndPrint(token: String) = viewModelScope.launch(Dispatchers.Default) {
        runCatching {
            println("START DOWNLOAD")
            isLoading = true
            val response =
                client.post("https://sandblast.natali37.ru/api/data-matrix/print") {
                    setBody(PdfRequest(token, versionDate))
                }
            val body = response.body<PdfResponse>()
            if (!body.isSuccess) throw Throwable(body.message)
            startPrintProcess(body.pdfBytes)
        }.onSuccess {
            println("SUCCESS")
            isLoading = false
            clearToken()
        }.onFailure {
            it.printStackTrace()
            print("FAIL1")
            error = it
            isLoading = false
        }
    }

    private fun startPrintProcess(file: ByteArray) {
        error = null
        runPrintIntent(file) {
            it.printStackTrace()
            print("FAIL2")
            error = it
        }
    }

    private fun runPrintIntent(file: ByteArray, onFailure: (e: Throwable) -> Unit = {}) {
        val document = PDDocument.load(file)
        val job = PrinterJob.getPrinterJob()
        job.setPageable(PDFPageable(document))
        if (job.printDialog()) runCatching {
            job.print()
            exitApplication()
        }.onFailure { e ->
            onFailure(e)
            clearToken()
        }
    }

    private fun clearToken() {
        token = ""
    }

    fun clearError() {
        error = null
    }

}