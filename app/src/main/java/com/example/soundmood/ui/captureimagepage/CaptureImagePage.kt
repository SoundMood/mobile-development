package com.example.soundmood.ui.captureimagepage

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.soundmood.data.PreferenceViewModel
import com.example.soundmood.databinding.ActivityCaptureImagePageBinding
import com.example.soundmood.databinding.ActivityMainBinding
import com.example.soundmood.network.ApiConfig
import com.example.soundmood.ui.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import android.Manifest
import java.text.SimpleDateFormat
import java.util.*


//////////////////////////////////////////////////////////////////////
class CaptureImagePage : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture
    private lateinit var binding: ActivityCaptureImagePageBinding

    private val preferenceViewModel: PreferenceViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }

    // Meminta permission untuk menggunakan camera
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureImagePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi kamera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Cek permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Untuk handle foto button
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.previewView.display.rotation)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("CaptureImagePage", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val photoFile = createFile(application.cacheDir, "IMG_${System.currentTimeMillis()}", ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CaptureImagePage, "Photo captured successfully!", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        val compressedFile = compressImage(photoFile)
                        sendImageToAPI(compressedFile)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CaptureImagePage", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun createFile(baseFolder: File, name: String, extension: String): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(baseFolder, "${name}_$timestamp$extension")
    }

    private suspend fun compressImage(imageFile: File): File {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)


                val compressedFile = createFile(application.cacheDir, "COMPRESSED_IMG_${System.currentTimeMillis()}", ".jpg")
                val fos = FileOutputStream(compressedFile)
                fos.write(outputStream.toByteArray())
                fos.flush()
                fos.close()

                compressedFile
            } catch (e: Exception) {
                Log.e("CaptureImagePage", "Compression failed: ${e.message}", e)
                imageFile
            }
        }
    }

    private suspend fun sendImageToAPI(imageFile: File) {
        try {
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val token = preferenceViewModel.appToken.toString()
            val accessToken = preferenceViewModel.accsessToken.toString()

            val response = ApiConfig.getApiService().generatePlaylist(
                appToken = token,
                accessToken = accessToken,
                image = imagePart
            )

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CaptureImagePage, "API Response: ${response.body()?.mood}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("CaptureImagePage", "API Error: ${response.code()} - ${response.message()}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CaptureImagePage, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("CaptureImagePage", "Exception: ${e.message}", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CaptureImagePage, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
