package com.involta.safeprint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.involta.safeprint.ui.screen.MainScreen
import com.involta.safeprint.ui.theme.AppTheme


@Composable
internal fun App() = AppTheme {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreen()
    }
}

