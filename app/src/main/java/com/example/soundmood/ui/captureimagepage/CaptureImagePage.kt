package com.example.soundmood.ui.captureimagepage

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.soundmood.databinding.ActivityCaptureImagePageBinding
import com.example.soundmood.ui.ViewModelFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.Manifest
import android.content.Intent
import androidx.camera.core.Preview
import com.example.soundmood.ui.moodresultpage.MoodResultActivity
import com.example.soundmood.util.Utility.Companion.createFile

class CaptureImagePage : AppCompatActivity(){
    private lateinit var cameraExecutors: ExecutorService
    private lateinit var imageCapture : ImageCapture

    private lateinit var binding : ActivityCaptureImagePageBinding

    private val captureViewModel: CaptureViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }


    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureImagePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutors = Executors.newSingleThreadExecutor()

        setupObserve()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionsLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun setupObserve() {
        captureViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        captureViewModel.playlistId.observe(this) { playlistId ->
            playlistId?.let {
                Toast.makeText(this, "Playlist generated: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CaptureImagePage", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val photoFile = createFile(applicationContext.cacheDir, "IMG", ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    captureViewModel.compressImage(photoFile, applicationContext) { compressedFile ->
                        captureViewModel.sendImageToAPI(compressedFile)
                    }

                    captureViewModel.playlistId.observe(this@CaptureImagePage) { playlistId ->
                        playlistId?.let {
                            Log.d("CaptureImagePage", "Playlist ID: $playlistId")
                            val intent = Intent(this@CaptureImagePage, MoodResultActivity::class.java)
                            intent.putExtra("PLAYLIST_ID", playlistId)
                            startActivity(intent)
                            finish()
                        }
                    }


                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CaptureImagePage, "Photo capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e("CaptureImagePage", "Photo capture failed", exception.cause)
                }


            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutors.shutdown()
    }
}


//////////////////////////////////////////////////////////////////////
//class CaptureImagePage : AppCompatActivity() {
//
//    private lateinit var cameraExecutor: ExecutorService
//    private lateinit var imageCapture: ImageCapture
//    private lateinit var binding: ActivityCaptureImagePageBinding
//
//    private val preferenceViewModel: PreferenceViewModel by viewModels {
//        ViewModelFactory(applicationContext)
//    }
//
//    // Meminta permission untuk menggunakan camera

//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCaptureImagePageBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Inisialisasi kamera executor
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        // Cek permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            startCamera()
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//
//        // Untuk handle foto button
//        binding.captureButton.setOnClickListener {
//            takePhoto()
//        }
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(binding.previewView.surfaceProvider)
//            }
//
//            imageCapture = ImageCapture.Builder()
//                .setTargetRotation(binding.previewView.display.rotation)
//                .build()
//
//            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//            } catch (e: Exception) {
//                Log.e("CaptureImagePage", "Use case binding failed", e)
//            }
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    private fun takePhoto() {
//        val photoFile = createFile(application.cacheDir, "IMG_${System.currentTimeMillis()}", ".jpg")
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    Toast.makeText(this@CaptureImagePage, "Photo captured successfully!", Toast.LENGTH_SHORT).show()
//
//                    lifecycleScope.launch {
//                        val compressedFile = compressImage(photoFile)
//                        sendImageToAPI(compressedFile)
//                    }
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Log.e("CaptureImagePage", "Photo capture failed: ${exception.message}", exception)
//                }
//            }
//        )
//    }
//
//    private fun createFile(baseFolder: File, name: String, extension: String): File {
//        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        return File(baseFolder, "${name}_$timestamp$extension")
//    }
//
//    private suspend fun compressImage(imageFile: File): File {
//        return withContext(Dispatchers.IO) {
//            try {
//                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
//
//                val outputStream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
//
//
//                val compressedFile = createFile(application.cacheDir, "COMPRESSED_IMG_${System.currentTimeMillis()}", ".jpg")
//                val fos = FileOutputStream(compressedFile)
//                fos.write(outputStream.toByteArray())
//                fos.flush()
//                fos.close()
//
//                compressedFile
//            } catch (e: Exception) {
//                Log.e("CaptureImagePage", "Compression failed: ${e.message}", e)
//                imageFile
//            }
//        }
//    }
//
//    private suspend fun sendImageToAPI(imageFile: File) {
//        try {
//            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
//
//            val token = preferenceViewModel.appToken.firstOrNull()
//            val accessToken = preferenceViewModel.accsessToken.firstOrNull()
//
//            val accessTokenPart = MultipartBody.Part.createFormData("access_token",accessToken.toString())
//
//            val response = ApiConfig.getSelfApi().generatePlaylist(
//                appToken = "Bearer $token",
//                accessToken = accessTokenPart,
//                image = imagePart
//            )
//
//            if (response.isSuccessful) {
//                withContext(Dispatchers.Main) {
//                    val playlistId = response.body()?.playlistId
//                    Log.d("CaptureImagePage","Playlist id : ${response.body()?.playlistId}")
//                    Toast.makeText(this@CaptureImagePage, "API Response: ${response.body()?.playlistId}", Toast.LENGTH_SHORT).show()
//
//                    val intent = Intent(this@CaptureImagePage,MoodResultActivity::class.java).apply {
//                        putExtra("PLAYLIST_ID",playlistId)
//                    }
//                    startActivity(intent)
//                    finish()
//                }
//            } else {
//                Log.e("CaptureImagePage", "API Error: ${response.code()} - ${response.message()}")
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@CaptureImagePage, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("CaptureImagePage", "Exception: ${e.message}", e)
//            withContext(Dispatchers.Main) {
//                Toast.makeText(this@CaptureImagePage, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//}
