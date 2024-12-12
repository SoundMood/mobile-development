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

//    fun compressImage(imageFile: File, context: Context, onCompressed: (File) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
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
//                withContext(Dispatchers.Main) {
//                    onCompressed(compressedFile)
//                }
////                // tes
////                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
////                val outputStream = ByteArrayOutputStream()
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
////
////                val compressedFile = createFile(context.cacheDir, "COMPRESSED_IMG_${System.currentTimeMillis()}", ".jpg")
////                val fos = FileOutputStream(compressedFile)
////                fos.write(outputStream.toByteArray())
////                fos.flush()
////                fos.close()
////
////                val fileSize = compressedFile.length()/1024
////                Log.d("CaptureViewModel","Compressed File $fileSize")
////
////                onCompressed(compressedFile)
//            } catch (e: Exception) {
//                Log.e("CaptureViewModel", "Compression failed: ${e.message}", e)
//                withContext(Dispatchers.Main) {
//                    _errorMessage.value = "Compression failed: ${e.message}"
//                }
//            }
//        }
//    }


    fun compressImage(imageFile: File, context: Context, onCompressed: (File) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeFile(imageFile.absolutePath, this)
                    inSampleSize = calculateInSampleSize(this, 800, 800) // Target resolusi 800x800
                    inJustDecodeBounds = false
                }

                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream) // Ubah kualitas ke 60

                val compressedFile = createFile(context.cacheDir, "COMPRESSED_IMG_${System.currentTimeMillis()}", ".jpg")
                val fos = FileOutputStream(compressedFile)
                fos.write(outputStream.toByteArray())
                fos.flush()
                fos.close()

                val fileSize = compressedFile.length() / 1024
                Log.d("CaptureViewModel", "Compressed File ${fileSize}KB")

                withContext(Dispatchers.Main) {
                    onCompressed(compressedFile)
                }
            } catch (e: Exception) {
                Log.e("CaptureViewModel", "Compression failed: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Compression failed: ${e.message}"
                }
            }
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
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