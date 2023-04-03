package com.involta.safeprint.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.involta.safeprint.extention.MarqueeText
import com.involta.safeprint.extention.rememberViewModel

@Composable
fun MainScreen(exitApplication: () -> Unit) {
    val viewModel = rememberViewModel<MainViewModel>()
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.onComposed(clipboardManager.getText().toString()) {
            exitApplication()
        }
    }
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(viewModel.isLoading, Modifier.align(Alignment.TopCenter)) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        VerticalScrollbar(
            rememberScrollbarAdapter(scrollState),
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(4.dp)
        )
        Column(
            modifier = Modifier.align(Alignment.Center).verticalScroll(scrollState).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MarqueeText(
                "Будьте внимательны, после ввода кода, данный файл уже будет невозможно распечатать повторно!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                gradientEdgeColor = MaterialTheme.colors.background
            )
            TextField(viewModel.token, {
                viewModel.onTokenChanged(it)
            }, enabled = viewModel.isTokenFieldEnabled, modifier = Modifier.fillMaxWidth(), label = {
                Text("Вставьте скопированный код с сайта")
            })
            Button(
                { viewModel.onTokenChanged(clipboardManager.getText().toString()) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isPasteButtonEnabled
            ) {
                Text("Вставить из буфера обмена")
            }
            AnimatedVisibility(viewModel.isErrorVisible) {
                Card(backgroundColor = MaterialTheme.colors.error) {
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(viewModel.error?.message ?: "Ошибка")
                        OutlinedButton({
                            viewModel.clearError()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}