package com.involta.safeprint.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.involta.safeprint.extention.rememberViewModel

@Composable
fun MainScreen() {
    val viewModel = rememberViewModel<MainViewModel>()
    LaunchedEffect(Unit) {
        viewModel.onComposed()
    }
    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(viewModel.isLoading, Modifier.align(Alignment.TopCenter)) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        Column(
            modifier = Modifier.align(Alignment.Center).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(viewModel.isButtonEnabled) {
                Text(
                    "Будьте внимательны, после нажатия на кнопку печать, данный файл уже будет невозможно распечатать повторно!",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            Button(
                onClick = {
                    viewModel.onPrintClicked()
                },
                enabled = viewModel.isButtonEnabled
            ) {
                Text("Печать")
            }
            AnimatedVisibility(viewModel.isErrorVisible) {
                Card(backgroundColor = MaterialTheme.colors.error, modifier = Modifier.padding(16.dp)) {
                    Column(
                        Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(viewModel.error?.message ?: "Ошибка")
                        Button({
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