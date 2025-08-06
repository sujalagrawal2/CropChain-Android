package com.hexagraph.cropchain.ui.screens.farmer.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexagraph.cropchain.domain.model.CropImages
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.data.repository.MetaMaskSDKRepositoryImpl
import com.hexagraph.cropchain.domain.model.RecentActivity
import com.hexagraph.cropchain.domain.repository.RecentActivityRepository
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeScreenUIState(
    val userName: String = "",
    val recentActivity: List<RecentActivity> = emptyList()
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cropRepository: CropRepository,
    private val appPreferences: AppPreferences,
    private val metaMaskSDKRepositoryImpl: MetaMaskSDKRepositoryImpl,
    private val recentActivityRepository: RecentActivityRepository,
    private val web3jRepository: Web3jRepository
) : ViewModel() {


    fun getAccount(): String {
        return metaMaskSDKRepositoryImpl.walletAddress
    }


    private val _uiState = mutableStateOf(HomeScreenUIState())
    val uiState: State<HomeScreenUIState> = _uiState

    init {
        viewModelScope.launch {
            appPreferences.username.getFlow().collectLatest {
                _uiState.value = _uiState.value.copy(userName = it)
            }
        }
        getRecentActivity()
    }

    private fun getRecentActivity() {
        viewModelScope.launch {
            recentActivityRepository.getAllActivities().collectLatest { activityList ->
                _uiState.value = _uiState.value.copy(recentActivity = activityList)

                for (activity in activityList) {
                    if ((activity.status == 0 || activity.status == 1 || activity.status == -2) && activity.transactionHash != null) {
                        startPollingForStatus(activity)
                    }
                }

            }
        }
    }

    private fun startPollingForStatus(activity: RecentActivity) {
        viewModelScope.launch {
            while (isActive) {
                val result = web3jRepository.getTransactionStatus(activity.transactionHash!!)

                result.fold(
                    onSuccess = { status ->
                        when (status) {
                            "Success" -> {
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = 2)
                                )
                                return@launch

                            }

                            "Pending" -> {
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = 1)
                                )
                                delay(3000)
                            }

                            else -> {
                                // Shouldn't happen, but break loop to stop polling
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = -1, reason =  "Unknown error")
                                )
                                return@launch

                            }
                        }
                    },
                    onFailure = { error ->
                        recentActivityRepository.updateActivity(
                            activity.copy(status = -1, reason = error.message ?: "Unknown error")
                        )
                        return@launch
                    }
                )
            }
        }
    }


}