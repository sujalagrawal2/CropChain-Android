package com.hexagraph.cropchain.data.local.apppreferences

import com.hexagraph.cropchain.domain.model.SupportedLanguages

interface AppPreferences {
    val aadharID: DataStorePreference<String>
    val username: DataStorePreference<String>
    val isUserLoggedIn: DataStorePreference<Boolean>
    val isCurrentUserFarmer: DataStorePreference<Boolean>
    val appLanguage: DataStorePreference<SupportedLanguages>
    val areAllPermissionsGranted: DataStorePreference<Boolean>
    val accountSelected : DataStorePreference<String>
    val metaMaskMessage : DataStorePreference<String>
    val deviceId : DataStorePreference<String>
    val token: DataStorePreference<String>
}