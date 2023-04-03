package com.involta.safeprint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.involta.safeprint.screen.MainScreen


@Composable
internal fun App(exitApplication: () -> Unit) = AppTheme {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreen(){
            exitApplication()
        }
    }
}

