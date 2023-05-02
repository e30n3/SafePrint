package com.involta.safeprint.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineScope

@Composable
inline fun <reified T : ViewModel> rememberViewModel(): T {
    val scope = rememberCoroutineScope()
    val viewModel by rememberSaveable { mutableStateOf(T::class.constructors.first().call(scope)) }
    return viewModel
}
abstract class ViewModel(private val viewModelScope: CoroutineScope)