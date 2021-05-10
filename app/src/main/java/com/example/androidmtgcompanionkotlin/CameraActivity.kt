package com.example.androidmtgcompanionkotlin
//https://codelabs.developers.google.com/codelabs/camerax-getting-started#1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidmtgcompanionkotlin.GraphicOverlay.Graphic
import com.google.mlkit.vision.common.InputImage

import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera_capture_button: Button
    private lateinit var viewFinder: PreviewView
    private lateinit var mGraphicOverlay: GraphicOverlay


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        viewFinder = findViewById(R.id.viewFinder)
        camera_capture_button = findViewById(R.id.camera_capture_button)
        mGraphicOverlay = findViewById(R.id.graphic_overlay)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
                outputDirectory,
                SimpleDateFormat(FILENAME_FORMAT, Locale.US
                ).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }
        })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                    }

            imageCapture = ImageCapture.Builder()
                    .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                            Log.d(TAG, "Average luminosity: $luma")
                        })
                    }


            val textAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                                cameraExecutor,
                                TextReaderAnalyzer(::onTextFound)
                        )
                    }
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            Log.d("dim", "width: " + viewFinder.width + " height:" + viewFinder.height)
            mGraphicOverlay.setCameraInfo(viewFinder.width, viewFinder.height, CameraCharacteristics.LENS_FACING_BACK)
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture, textAnalyzer)

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))


    }
    private fun onTextFound(foundText: List<TextBlock>)  {

        if (foundText.isEmpty()) {
            Toast.makeText(baseContext, "No text found", Toast.LENGTH_SHORT).show()
            return
        }
        mGraphicOverlay!!.clear()
        for (i in 0 until foundText.count()) {
            val lines: List<Text.Line> = foundText[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic: Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay!!.add(textGraphic)
                }
            }
        }

//        for (block in foundText) {
//                // You can access whole block of text using block.text
//                val blockText = block.text
//                val blockCornerPoints = block.cornerPoints
//                val blockFrame = block.boundingBox
//            Log.d(TAG, "We got new text: $blockText")
//
//                for (line in block.lines) {
//                    // You can access whole line of text using line.text
//                    //textFoundListener(line.text)
//                    val lineText = line.text
//                    val lineCornerPoints = line.cornerPoints
//                    val lineFrame = line.boundingBox
//                    for (element in line.elements) {
//                        val elementText = element.text
//                        val elementCornerPoints = element.cornerPoints
//                        val elementFrame = element.boundingBox
//                        //textFoundListener(element.text)
//
//
//                    }
//                }
//            }

    }
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener(luma)

            image.close()
        }
    }
    //https://towardsdatascience.com/create-a-google-lens-like-real-time-text-in-image-recognition-android-app-using-camerax-and-ml-kit-eac6d286050f
    class TextReaderAnalyzer(
            //context: Context
            //private val textFoundListener: (String) -> Unit
            private val textFoundListener: (List<TextBlock>) -> Unit

    ) : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            imageProxy.image?.let { process(it, imageProxy) }
        }

        private fun process(image: Image, imageProxy: ImageProxy) {
            try {
                readTextFromImage(InputImage.fromMediaImage(image, 90), imageProxy)
            } catch (e: IOException) {
                Log.d(TAG, "Failed to load the image")
                e.printStackTrace()
            }
        }

        private fun readTextFromImage(image: InputImage, imageProxy: ImageProxy) {
            TextRecognition.getClient()
                    .process(image)
                    .addOnSuccessListener { visionText ->
                        processTextFromImage(visionText, imageProxy)
                        imageProxy.close()
                    }
                    .addOnFailureListener { error ->
                        Log.d(TAG, "Failed to process the image")
                        error.printStackTrace()
                        imageProxy.close()
                    }
        }

        private fun processTextFromImage(visionText: Text, imageProxy: ImageProxy) {

            val blocks: List<TextBlock> = visionText.textBlocks
            if (blocks.isEmpty()) {
                //Toast.makeText(context,"No text found", Toast.LENGTH_SHORT).show()
                return
            }
            textFoundListener(blocks)


        //paintCanvasImageOverlay(visionText, imageProxy)

//            for (block in visionText.textBlocks) {
//                // You can access whole block of text using block.text
//                val blockText = block.text
//                val blockCornerPoints = block.cornerPoints
//                val blockFrame = block.boundingBox
//
//                for (line in block.lines) {
//                    // You can access whole line of text using line.text
//                    //textFoundListener(line.text)
//                    val lineText = line.text
//                    val lineCornerPoints = line.cornerPoints
//                    val lineFrame = line.boundingBox
//                    for (element in line.elements) {
//                        val elementText = element.text
//                        val elementCornerPoints = element.cornerPoints
//                        val elementFrame = element.boundingBox
//                        //textFoundListener(element.text)
//                    }
//                }
//            }
        }
        private fun paintCanvasImageOverlay(visionText: Text, imageProxy: ImageProxy) {
//            @androidx.camera.core.ExperimentalGetImage
//            val image:Image? = imageProxy.getImage()
//
//
//
//            @androidx.camera.core.ExperimentalGetImage
//            if (image!=null) {
//                val buffer = image.planes[0].buffer
//                val bytes = ByteArray(buffer.capacity())
//                buffer[bytes]
//                val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
//                val mutableBitmap:Bitmap = bitmapImage.copy(bitmapImage.config, true)
//                val blocks = visionText.textBlocks
//                val blocksRect = visionText.textBlocks.mapNotNull { it.boundingBox }
//
//
//                val lines = blocks.flatMap { it.lines }
//                val linesRect = lines.mapNotNull { it.boundingBox }
//
//                val elements = lines.flatMap { it.elements }
//                val elementsRect = elements.mapNotNull { it.boundingBox }
//                with(Canvas(mutableBitmap)) {
//                    val paint = Paint()
//                    paint.setARGB(255, 255, 255, 255)
//                    blocksRect.forEach {
//                        drawRect(it, paint)
//                    }
//                    linesRect.forEach { drawRect(it, paint) }
//                    elementsRect.forEach { drawRect(it, paint) }
//                }
//                //imageView.setImageBitmap(mutableBitmap)
//            }


        }
    }
}