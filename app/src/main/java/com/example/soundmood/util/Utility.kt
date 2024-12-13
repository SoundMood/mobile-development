package com.example.soundmood.util

import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Utility {
    companion object{
        fun parseIso8601ToEpoch(iso8601String: String): Long {
            return try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = dateFormat.parse(iso8601String)
                date?.time?.div(1000) ?: 0L // Convert to seconds
            } catch (e: Exception) {
                Log.e("HomePageViewModel", "Error parsing expiryAt: ${e.message}")
                0L
            }
        }
        fun createFile(baseFolder: File, name: String, extension: String): File {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            return File(baseFolder, "${name}_$timestamp$extension")
        }

        val albumIds = listOf(
            "1zCNrbPpz5OLSr6mSpPdKm", "34OkZVpuzBa9y40DCy0LPR", "3T4tUhGYeRNVUGevb0wThu",
            "1ATL5GLyefJaxhQzSPVrLX", "4yP0hdKOZPNshxUOjY0cZj", "0S0KGZnfBGSIssfF54WSJh",
            "5r36AJ6VOJtp00oxSkBZ5h", "6mJZTV8lCqnwftYZa94bXS", "6trNtQUgC8cgbWcqoMYkOR",
            "6s84u2TUpR3wdUv4NgKA2j", "3cfAM8b8KqJRoIzt3zLKqw", "5eyZZoQEFQWRHkV2xgAeBw",
            "20r762YmB5HeofjMCiPMLv", "5CnpZV3q5BcESefcB3WJmz", "41GuZcammIkupMPKH2OJ6I"
        )
    }

}