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
    private val activePollingJobs = mutableSetOf<String>()
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
                    val txHash = activity.transactionHash ?: continue

                    if ((activity.status == 0 || activity.status == 1 || activity.status == -2) && !activePollingJobs.contains(txHash)) {
                        activePollingJobs.add(txHash)
                        startPollingForStatus(activity)
                    }
                }
            }
        }
    }

    private fun startPollingForStatus(activity: RecentActivity) {
        val txHash = activity.transactionHash!!
        viewModelScope.launch {
            while (isActive) {
                val result = web3jRepository.getTransactionStatus(txHash)

                result.fold(
                    onSuccess = { status ->
                        when (status) {
                            "Success" -> {
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = 2)
                                )
                                activePollingJobs.remove(txHash)
                                return@launch
                            }

                            "Pending" -> {
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = 1)
                                )
                                delay(3000)
                            }

                            else -> {
                                recentActivityRepository.updateActivity(
                                    activity.copy(status = -1, reason = "Unknown error")
                                )
                                activePollingJobs.remove(txHash)
                                return@launch
                            }
                        }
                    },
                    onFailure = { error ->
                        if (error.message?.contains("429") == true) {
                            delay(10_000) // back off
                        } else {
                            recentActivityRepository.updateActivity(
                                activity.copy(status = -1, reason = error.message ?: "Unknown error")
                            )
                            activePollingJobs.remove(txHash)
                            return@launch
                        }
                    }
                )
            }
        }
    }



}