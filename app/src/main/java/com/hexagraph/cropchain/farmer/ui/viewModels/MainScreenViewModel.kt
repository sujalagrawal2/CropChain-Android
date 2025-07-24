package com.hexagraph.cropchain.farmer.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.repository.apppreferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val appPreferences: AppPreferences) :
    ViewModel() {

    fun getMetaMaskMessage(): Flow<String> {
        return appPreferences.metaMaskMessage.getFlow()
    }

    fun setMetaMaskMessageToDefault() {
        viewModelScope.launch {
            appPreferences.metaMaskMessage.set("")
        }
    }

}