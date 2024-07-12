package com.example.weatherapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkConnectivityUC: com.example.domain.usecase.ObserveConnectivityUC,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _connectivityState = MutableStateFlow<Boolean>(false)
    val connectivityState: StateFlow<Boolean> get() = _connectivityState

    init {
        viewModelScope.launch {
            checkConnectivityUC.invoke().collect { connectivity ->
                _connectivityState.value = connectivity
            }
        }
    }
}