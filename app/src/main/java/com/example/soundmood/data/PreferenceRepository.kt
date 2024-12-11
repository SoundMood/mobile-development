package com.example.soundmood.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore_preference")

class PreferenceRepository(private val context : Context) {

    companion object{
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
        private val APP_TOKEN = stringPreferencesKey("app_token")
        private val ACCESS_TOKEN_EXPIRY = stringPreferencesKey("access_token_expiry")
        private val APP_TOKEN_EXPIRY = stringPreferencesKey("app_token_expiry")
    }

    // Menyimpan app token
    suspend fun saveAppToken(appToken:String){
        context.dataStore.edit { preference->
            Log.d("PreferenceRepository","Saving app token $appToken")
            preference[APP_TOKEN] = appToken
        }
    }

    // Membaca app token
    val appTokenFlow : Flow<String?> = context.dataStore.data.map { preference->
        preference[APP_TOKEN]
    }

    // Menyimpan access token
    suspend fun saveAccessToken(token : String){
        context.dataStore.edit{preference->
            Log.d("PreferenceRepository","Saving access token $token")
            preference[ACCESS_TOKEN] = token
        }
    }

    // Mengambil access token expiry
    val accessTokenExpiry : Flow<Long> = context.dataStore.data.map { preference->
        preference[APP_TOKEN_EXPIRY]?.toLongOrNull() ?: 0
    }

    // Menyimpan access token expiry
    suspend fun saveAccesTokenExpiry(expiry:Long){
        context.dataStore.edit { preference->
            preference[ACCESS_TOKEN_EXPIRY] = expiry.toString()
        }
    }

    // Mengambil app token expiry
    val appTokenExpiry : Flow<Long> = context.dataStore.data.map { preference->
        preference[APP_TOKEN_EXPIRY]?.toLongOrNull() ?:0
    }

    // Menyimpan app token expiry
    suspend fun saveAppTokenExpiry(expiry: Long){
        context.dataStore.edit { preference->
            preference[APP_TOKEN_EXPIRY] = expiry.toString()
        }
    }

    // Membaca access token
    val accsessTokenFlow : Flow<String?> = context.dataStore.data.map{ preference->
        preference[ACCESS_TOKEN]
    }

    // Meyimpan dark mode
    suspend fun saveDarkMode(isDark:Boolean){
        context.dataStore.edit { preference->
            preference[DARK_MODE]=isDark
        }
    }

    // Membaca dark mode
    val darkModeFlow : Flow<Boolean> = context.dataStore.data.map { preference->
        preference[DARK_MODE]?:false
    }

    // Menghapus access token
    suspend fun clearAccessToken(token:String){
        context.dataStore.edit { preference->
            preference.remove(ACCESS_TOKEN)
        }
    }

    // Menghapus app token
    suspend fun clearAppToken(token:String){
        context.dataStore.edit { preference->
            preference.remove(APP_TOKEN)
        }
    }
}