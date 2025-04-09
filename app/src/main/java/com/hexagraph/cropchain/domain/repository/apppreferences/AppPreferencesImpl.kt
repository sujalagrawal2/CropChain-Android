package com.hexagraph.cropchain.domain.repository.apppreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hexagraph.cropchain.domain.model.SupportedLanguages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferencesImpl(private val context: Context): AppPreferences {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        name = "settings"
    )
    private object PreferenceKeys {
        val AADHAR_ID = stringPreferencesKey("username")
        val APP_LANGUAGE = intPreferencesKey("language")
        val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
        val IS_CURRENT_USER_FARMER = booleanPreferencesKey("is_current_user_farmer")
    }


    override val aadharID: DataStorePreference<String>
        get() = object : DataStorePreference<String>{
            override fun getFlow(): Flow<String> {
                return context.datastore.data.map {
                    it[PreferenceKeys.AADHAR_ID] ?: ""
                }
            }

            override suspend fun set(value: String) {
                context.datastore.edit { prefs->
                    prefs[PreferenceKeys.AADHAR_ID] = value
                }
            }
        }

    override val isUserLoggedIn: DataStorePreference<Boolean>
        get() = object : DataStorePreference<Boolean>{
            override fun getFlow(): Flow<Boolean> {
                return context.datastore.data.map {
                    it[PreferenceKeys.IS_USER_LOGGED_IN] ?: false
                }
            }

            override suspend fun set(value: Boolean) {
                context.datastore.edit {
                    it[PreferenceKeys.IS_USER_LOGGED_IN] = value
                }
            }

        }

    override val appLanguage: DataStorePreference<SupportedLanguages>
        get() = object : DataStorePreference<SupportedLanguages> {
            override fun getFlow(): Flow<SupportedLanguages> {
                return context.datastore.data.map { prefs ->
                    val langID = prefs[PreferenceKeys.APP_LANGUAGE] ?: SupportedLanguages.ENGLISH.langID
                    SupportedLanguages.entries.find { it.langID == langID } ?: SupportedLanguages.ENGLISH
                }
            }

            override suspend fun set(value: SupportedLanguages) {
                context.datastore.edit { prefs ->
                    prefs[PreferenceKeys.APP_LANGUAGE] = value.langID
                }
            }
        }

    override val isCurrentUserFarmer: DataStorePreference<Boolean>
        get() = object : DataStorePreference<Boolean> {
            override fun getFlow(): Flow<Boolean> {
                return context.datastore.data.map { prefs ->
                    prefs[PreferenceKeys.IS_CURRENT_USER_FARMER] ?: false
                }
            }

            override suspend fun set(value: Boolean) {
                context.datastore.edit { prefs ->
                    prefs[PreferenceKeys.IS_CURRENT_USER_FARMER] = value
                }
            }
        }
}