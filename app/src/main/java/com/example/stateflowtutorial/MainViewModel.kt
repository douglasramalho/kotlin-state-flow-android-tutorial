package com.example.stateflowtutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    private var isError = false

    fun getSomething() = viewModelScope.launch {
        _uiStateFlow.value = UiState.Loading
        delay(5000L)

        isError = Random.nextBoolean()

        _uiStateFlow.value = if (isError) {
            UiState.Error
        } else UiState.Success
    }

    sealed class UiState {
        object Success : UiState()
        object Error : UiState()
        object Loading : UiState()
        object Initial : UiState()
    }
}