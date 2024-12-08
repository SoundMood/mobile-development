package com.example.soundmood.ui.captureimagepage

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
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
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityCaptureImagePageBinding
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CaptureImagePage : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var binding : ActivityCaptureImagePageBinding

    companion object{
        val CAMERA_PERMISSION_REQUEST_CODE = 1234
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_image_page)
        binding = ActivityCaptureImagePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = binding.previewView
        cameraExecutor = Executors.newSingleThreadExecutor()
        checkCameraPermission()
        startCamera()

        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val outputStream = ByteArrayOutputStream()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputStream).build()

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = imageProxyToBitmap(image)
                    image.close()

                    Toast.makeText(this@CaptureImagePage,"Success",Toast.LENGTH_SHORT).show()

//                    // Kirim bitmap ke API
//                    sendImageToAPI(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    // sendImage to API
//    private fun sendImageToAPI(bitmap: Bitmap) {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        val byteArray = outputStream.toByteArray()
//
//        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)
//        val imagePart = MultipartBody.Part.createFormData("image", "photo.jpg", requestBody)
//
//        lifecycleScope.launch {
//            try {
//                // Panggil API dan tangkap respons string
//                val responseBody = ApiConfig.getSelfApi().uploadImage(imagePart)
//                val responseString = responseBody.string() // Ubah ResponseBody ke string
//
//                // Tampilkan respons di Toast
//                Toast.makeText(this@CaptureImagePage, "Response: $responseString", Toast.LENGTH_LONG).show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(this@CaptureImagePage, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }



}