package com.example.soundmood.data

import android.content.Context
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
    }

    // Menyimpan token
    suspend fun saveAccessToken(token : String){
        context.dataStore.edit{preference->
            preference[ACCESS_TOKEN] = token
        }
    }

    // Membaca token
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
}