package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ObserveConnectivityUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkConnectivityUC: ObserveConnectivityUC
) : ViewModel() {

    private val _connectivityState = MutableStateFlow(false)
    val connectivityState: StateFlow<Boolean> get() = _connectivityState

    init {
        viewModelScope.launch {
            checkConnectivityUC.invoke().collect { connectivity ->
                _connectivityState.value = connectivity
            }
        }
    }
}