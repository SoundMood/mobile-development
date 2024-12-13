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
        binding.progressBar.visibility= android.view.View.VISIBLE
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        val user_id = intent.getStringExtra("user_id")
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
                            intent.putExtra("USER_ID", user_id)

                            startActivity(intent)
                            finish()
                        }
                    }
                    binding.progressBar.visibility= android.view.View.GONE


                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CaptureImagePage, "Photo capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e("CaptureImagePage", "Photo capture failed", exception.cause)
                    binding.progressBar.visibility= android.view.View.GONE
                }


            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutors.shutdown()
    }
}

