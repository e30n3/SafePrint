package com.involta.safeprint.screen

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.involta.safeprint.extention.ViewModel
import com.involta.safeprint.fileName
import com.involta.safeprint.pdfUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.printing.PDFPageable
import java.awt.print.PrinterJob
import java.io.File

class MainViewModel(private val viewModelScope: CoroutineScope) : ViewModel(viewModelScope) {

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<Throwable?>(null)
        private set

    val isErrorVisible by derivedStateOf { error != null }

    val isButtonEnabled by derivedStateOf { !isLoading }
    fun onComposed() {
        downloadPdf()
    }

    private fun downloadPdf(url: String = pdfUrl) = viewModelScope.launch {
        runCatching {
            println("START DOWNLOAD")
            isLoading = true
            val client = HttpClient()
            val response = client.get {
                url(url)
            }.readBytes()
            val file = File(fileName)
            file.deleteOnExit()
            file.writeBytes(response)
            client.close()
        }.onSuccess {
            println("SUCCESS")
            isLoading = false
        }.onFailure {
            println(it.message ?: "")
            error = it
            isLoading = false
        }
    }

    fun onPrintClicked() {
        error = null
        runPrintIntent(File(fileName)) {
            println(it.message ?: "")
            error = it
        }
    }

    private fun runPrintIntent(file: File, onFailure: (e: Throwable) -> Unit = {}) {
        val document = PDDocument.load(file)
        val job = PrinterJob.getPrinterJob()
        job.setPageable(PDFPageable(document))
        if (job.printDialog()) runCatching {
            job.print()
        }.onFailure { e ->
            onFailure(e)
        }
    }

    fun clearError() {
        error = null
    }

}