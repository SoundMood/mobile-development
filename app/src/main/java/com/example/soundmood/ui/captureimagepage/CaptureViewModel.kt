package com.example.soundmood.ui.captureimagepage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.network.ApiConfig
import com.example.soundmood.util.Utility.Companion.createFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CaptureViewModel(private val preferenceViewModel: PreferenceViewModel): ViewModel() {
    private val _playlistId = MutableLiveData<String?>()
    val playlistId: LiveData<String?> = _playlistId

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun compressImage(imageFile: File, context: Context, onCompressed: (File) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
//                val outputStream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
//
//                val compressedFile = createFile(context.cacheDir, "COMPRESSED_IMG_${System.currentTimeMillis()}", ".jpg")
//                val fos = FileOutputStream(compressedFile)
//                fos.write(outputStream.toByteArray())
//                fos.flush()
//                fos.close()
//
//                val fileSize = compressedFile.length()/1024
//                Log.d("CaptureViewModel","Compressed File $fileSize")
//
//                if(fileSize in 1..200){
//                    withContext(Dispatchers.Main) {
//                        onCompressed(compressedFile)
//                    }
//                }else{
//                    Log.d("CaptureViewModel","FileSize more than 200kb")
//                }
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)

                var quality = 75 // Mulai dengan kualitas 75
                var compressedFile: File
                var fileSize: Long

                do {
                    // Kompres gambar dengan kualitas yang diatur
                    outputStream.reset() // Reset output stream sebelum menulis ulang
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

                    // Membuat file untuk gambar terkompresi
                    compressedFile = createFile(context.cacheDir, "RESIZED_IMG_${System.currentTimeMillis()}", ".jpg")
                    val fos = FileOutputStream(compressedFile)
                    fos.write(outputStream.toByteArray())
                    fos.flush()
                    fos.close()

                    // Mendapatkan ukuran file dalam KB
                    fileSize = compressedFile.length() / 1024

                    // Jika ukuran file lebih besar dari 500 KB, kurangi kualitasnya
                    quality -= 5
                    Log.d("CaptureViewModel", "Compressed File Size: $fileSize KB at quality $quality")

                } while (fileSize > 500 && quality > 5) // Teruskan hingga ukuran file kurang dari 500 KB atau kualitas mencapai 5%

                if (fileSize <= 500) {
                    withContext(Dispatchers.Main) {
                        onCompressed(compressedFile)
                    }
                } else {
                    Log.d("CaptureViewModel", "Failed to compress below 500KB")
                }

            } catch (e: Exception) {
                Log.e("CaptureViewModel", "Compression failed: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Compression failed: ${e.message}"
                }
            }
        }
    }

    fun sendImageToAPI(imageFile: File) {
        viewModelScope.launch {
            try {
                val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                val token = preferenceViewModel.appToken.firstOrNull()
                val accessToken = preferenceViewModel.accsessToken.firstOrNull()

                val accessTokenPart = MultipartBody.Part.createFormData("access_token", accessToken.toString())

                val response = ApiConfig.getSelfApi().generatePlaylist(
                    appToken = "Bearer $token",
                    accessToken = accessTokenPart,
                    image = imagePart
                )

                if (response.isSuccessful) {
                    _playlistId.value = response.body()?.playlistId
                } else {
                    Log.e("CaptureViewModel", "API Error: ${response.code()} - ${response.message()}")
                    _errorMessage.value = "API Error: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("CaptureViewModel", "Exception: ${e.message}", e)
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

}