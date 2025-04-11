package com.hexagraph.cropchain.domain.repository.apppreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.hexagraph.cropchain.domain.model.SupportedLanguages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.getValue

class AppPreferencesImpl(private val context: Context): AppPreferences {
    private val datastore = DataStoreProvider.getInstance(context)
    private object PreferenceKeys {
        val AADHAR_ID = stringPreferencesKey("aadhar_id")
        val USERNAME = stringPreferencesKey("username")
        val APP_LANGUAGE = intPreferencesKey("language")
        val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
        val IS_CURRENT_USER_FARMER = booleanPreferencesKey("is_current_user_farmer")
        val ARE_ALL_PERMISSIONS_GRANTED = booleanPreferencesKey("are_all_permissions_granted")
    }


    override val aadharID: DataStorePreference<String>
        get() = object : DataStorePreference<String>{
            override fun getFlow(): Flow<String> {
                return datastore.data.map {
                    it[PreferenceKeys.AADHAR_ID] ?: ""
                }
            }

            override suspend fun set(value: String) {
                datastore.edit { prefs->
                    prefs[PreferenceKeys.AADHAR_ID] = value
                }
            }
        }

    override val isUserLoggedIn: DataStorePreference<Boolean>
        get() = object : DataStorePreference<Boolean>{
            override fun getFlow(): Flow<Boolean> {
                return datastore.data.map {
                    it[PreferenceKeys.IS_USER_LOGGED_IN] ?: false
                }
            }

            override suspend fun set(value: Boolean) {
                datastore.edit {
                    it[PreferenceKeys.IS_USER_LOGGED_IN] = value
                }
            }

        }

    override val appLanguage: DataStorePreference<SupportedLanguages>
        get() = object : DataStorePreference<SupportedLanguages> {
            override fun getFlow(): Flow<SupportedLanguages> {
                return datastore.data.map { prefs ->
                    val langID = prefs[PreferenceKeys.APP_LANGUAGE] ?: SupportedLanguages.ENGLISH.langID
                    SupportedLanguages.entries.find { it.langID == langID } ?: SupportedLanguages.ENGLISH
                }
            }

            override suspend fun set(value: SupportedLanguages) {
                datastore.edit { prefs ->
                    prefs[PreferenceKeys.APP_LANGUAGE] = value.langID
                }
            }
        }

    override val isCurrentUserFarmer: DataStorePreference<Boolean>
        get() = object : DataStorePreference<Boolean> {
            override fun getFlow(): Flow<Boolean> {
                return datastore.data.map { prefs ->
                    prefs[PreferenceKeys.IS_CURRENT_USER_FARMER] ?: false
                }
            }

            override suspend fun set(value: Boolean) {
                datastore.edit { prefs ->
                    prefs[PreferenceKeys.IS_CURRENT_USER_FARMER] = value
                }
            }
        }

    override val areAllPermissionsGranted: DataStorePreference<Boolean>
        get() = object : DataStorePreference<Boolean> {
            override fun getFlow(): Flow<Boolean> {
                return datastore.data.map { prefs ->
                    prefs[PreferenceKeys.ARE_ALL_PERMISSIONS_GRANTED] == true
                }
            }

            override suspend fun set(value: Boolean) {
                datastore.edit { prefs ->
                    prefs[PreferenceKeys.ARE_ALL_PERMISSIONS_GRANTED] = value
                }
            }
        }

    override val username: DataStorePreference<String>
        get() = object : DataStorePreference<String> {
            override fun getFlow(): Flow<String> {
                return datastore.data.map { prefs ->
                    prefs[PreferenceKeys.USERNAME] ?: ""
                }
            }

            override suspend fun set(value: String) {
                datastore.edit { prefs ->
                    prefs[PreferenceKeys.USERNAME] = value
                }
            }
        }
}


object DataStoreProvider {
    private val dataStoreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Volatile
    private var INSTANCE: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>? = null

    fun getInstance(context: Context): androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: PreferenceDataStoreFactory.create(
                scope = dataStoreScope
            ) {
                context.preferencesDataStoreFile("settings")
            }.also { INSTANCE = it }
        }
    }
}