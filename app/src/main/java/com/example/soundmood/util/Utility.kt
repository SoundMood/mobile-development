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
    }

}