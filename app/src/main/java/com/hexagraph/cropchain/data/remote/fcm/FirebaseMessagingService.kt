package com.hexagraph.cropchain.data.remote.fcm

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService() :
    FirebaseMessagingService() {
    @Inject
    lateinit var appPreferences: AppPreferences

    @ApplicationContext
    lateinit var context: Context

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Firebase Messaging Service", "Refreshed token: $token")

        CoroutineScope(Dispatchers.Default).launch {
            appPreferences.token.set(token)
        }

    }

}

