package com.hexagraph.cropchain.ui.navigation.farmer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.util.sendRegistrationToServer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NavHostViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val appPreferences: AppPreferences
) :
    ViewModel() {

    fun getMetaMaskMessage(): Flow<String> {
        return appPreferences.metaMaskMessage.getFlow()
    }

    fun setMetaMaskMessageToDefault() {
        viewModelScope.launch {
            appPreferences.metaMaskMessage.set("")
        }
    }

    init {
        viewModelScope.launch {
            appPreferences.token.getFlow().collect { token ->
                if (token != "") {
                    val deviceId = getOrCreateDeviceId(context)
                    val aadharNumber = appPreferences.aadharID.get()
                    if (deviceId != "" && aadharNumber != "") {
                        sendRegistrationToServer(
                            token,
                            aadharNumber,
                            deviceId,
                            context
                        ).onSuccess { response ->
                            Log.d("FCM", "Token upload successful: $response")
                            appPreferences.token.set("")
                        }.onFailure { exception ->
                            Log.e("FCM", "Token upload failed", exception)
                        }

                    }
                }
            }
        }


    }
}

@SuppressLint("UseKtx")
fun getOrCreateDeviceId(context: Context): String {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val existingId = prefs.getString("device_id", null)
    return existingId ?: UUID.randomUUID().toString().also {
        prefs.edit().putString("device_id", it).apply()
    }
}