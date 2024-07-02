//package com.itssvkv.todolist.utils
//
//import android.content.Context
//import android.content.res.AssetFileDescriptor
//import android.content.res.AssetManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Log
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.FileInputStream
//import java.io.IOException
//import java.io.InputStream
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//
//
//
//object com.itssvkv.todolist.utils.TfLiteManager {
//    private lateinit var tflite: Interpreter
//
//    fun testTfLite(context: Context) {
//        val assetManager: AssetManager = context.assets
//        try {
//            tflite = Interpreter(loadModelFile(context = context))
//            // Example: Run inference with a specific image
//            runInference("4x.png", context = context)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//
//    @Throws(IOException::class)
//    fun loadModelFile(context: Context): MappedByteBuffer {
//        val assetManager: AssetManager = context.assets
//        val fileDescriptor: AssetFileDescriptor = assetManager.openFd("model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel: FileChannel = inputStream.channel
//        val startOffset: Long = fileDescriptor.startOffset
//        val declaredLength: Long = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//
//
//    fun runInference(imageName: String, context: Context) {
//        // Load and preprocess the image
//        val bitmap = loadBitmapFromAsset(imageName, context = context)
//        val tensorImage = TensorImage(DataType.FLOAT32)
//        tensorImage.load(bitmap)
//
//        // Prepare input and output tensors
//        val inputShape = tflite.getInputTensor(0).shape()
//        val imageSizeX = inputShape[1]
//        val imageSizeY = inputShape[2]
//        val input = tensorImage.bitmap
//
//        // Run inference
//        val inputArray = arrayOf(input)
//        val outputMap: MutableMap<Int, Any> = HashMap()
//        val output = TensorBuffer.createFixedSize(intArrayOf(1, 1000), DataType.FLOAT32)
//        outputMap[0] = output.buffer.rewind()
//        tflite.runForMultipleInputsOutputs(inputArray, outputMap)
//
//        // Post-processing: Get the predicted label
//        val results = output.floatArray
//        val predictedClass = argmax(results) // Assuming argmax function is defined
//
//        // Print or display the predicted label
//        Log.d("Prediction", "Predicted label: $predictedClass")
//    }
//
//    // Utility function to load a Bitmap from the assets folder
//    fun loadBitmapFromAsset(fileName: String, context: Context): Bitmap {
//        val assetManager: AssetManager = context.assets
//        val inputStream: InputStream
//        var bitmap: Bitmap? = null
//        try {
//            inputStream = assetManager.open(fileName)
//            bitmap = BitmapFactory.decodeStream(inputStream)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return bitmap!!
//    }
//
//    // Utility function to find the index of the maximum value in an array
//    fun argmax(probabilities: FloatArray): Int {
//        var maxIndex = -1
//        var maxProbability = 0.0f
//        for (i in probabilities.indices) {
//            if (probabilities[i] > maxProbability) {
//                maxProbability = probabilities[i]
//                maxIndex = i
//            }
//        }
//        return maxIndex
//    }
//}
//package com.itssvkv.todolist.utils
//
//import android.content.Context
//import android.content.res.AssetFileDescriptor
//import android.content.res.AssetManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Log
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//import java.io.FileInputStream
//import java.io.IOException
//import java.io.InputStream
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//
//object com.itssvkv.todolist.utils.TfLiteManager {
//    private lateinit var tflite: Interpreter
//
//    fun testTfLite(context: Context) {
//        try {
//            tflite = Interpreter(loadModelFile(context = context))
//            // Example: Run inference with a specific image
//            runInference("3x.png", context = context)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    @Throws(IOException::class)
//    fun loadModelFile(context: Context): MappedByteBuffer {
//        val assetManager: AssetManager = context.assets
//        val fileDescriptor: AssetFileDescriptor = assetManager.openFd("model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel: FileChannel = inputStream.channel
//        val startOffset: Long = fileDescriptor.startOffset
//        val declaredLength: Long = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    fun runInference(imageName: String, context: Context) {
//        // Load and preprocess the image
//        val bitmap = loadBitmapFromAsset(imageName, context = context) ?: return
//        val tensorImage = TensorImage(DataType.FLOAT32)
//        tensorImage.load(bitmap)
//
//        // Prepare input and output tensors
//        val inputShape = tflite.getInputTensor(0).shape()
//        val imageSizeX = inputShape[1]
//        val imageSizeY = inputShape[2]
//        val input = tensorImage.tensorBuffer.buffer
//
//        // Run inference
//        val output = TensorBuffer.createFixedSize(intArrayOf(1, 1000), DataType.FLOAT32)
//        tflite.run(input, output.buffer.rewind())
//
//        // Post-processing: Get the predicted label
//        val results = output.floatArray
//        val predictedClass = argmax(results) // Assuming argmax function is defined
//
//        // Print or display the predicted label
//        Log.d("Prediction", "Predicted label: $predictedClass")
//    }
//
//    // Utility function to load a Bitmap from the assets folder
//    fun loadBitmapFromAsset(fileName: String, context: Context): Bitmap? {
//        val assetManager: AssetManager = context.assets
//        return try {
//            val inputStream: InputStream = assetManager.open(fileName)
//            BitmapFactory.decodeStream(inputStream)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    // Utility function to find the index of the maximum value in an array
//    fun argmax(probabilities: FloatArray): Int {
//        var maxIndex = -1
//        var maxProbability = -Float.MAX_VALUE
//        for (i in probabilities.indices) {
//            if (probabilities[i] > maxProbability) {
//                maxProbability = probabilities[i]
//                maxIndex = i
//            }
//        }
//        return maxIndex
//    }
//}
package com.itssvkv.todolist.utils

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

object TfLiteManager {
    private lateinit var interpreter: Interpreter

    fun testModel(context: Context) {
        val model = loadModelFile(context, "model.tflite")
        interpreter = Interpreter(model)
    }

    // Load the model from assets
    private fun loadModelFile(context: Context, modelName: String): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd(modelName)
        val fileInputStream = assetFileDescriptor.createInputStream()
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(bitmap: Bitmap): Int {
        // Get the input tensor shape from the interpreter
        val inputShape = interpreter.getInputTensor(0).shape()
        val inputSize = inputShape[1] // assuming square input, width = height
        val inputChannels = inputShape[3] // usually 3 (RGB) or 1 (grayscale)

        // Resize the bitmap to the expected input size of the model
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

        // Convert the bitmap to TensorImage
        val inputImageBuffer = TensorImage(DataType.UINT8)
        inputImageBuffer.load(resizedBitmap)

        // Prepare the input buffer
        val inputBuffer = inputImageBuffer.buffer

        // Prepare the output buffer
        val outputShape = interpreter.getOutputTensor(0).shape()
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32)

        // Run the model
        interpreter.run(inputBuffer, outputBuffer.buffer)

        // Get the predicted class
        val outputArray = outputBuffer.floatArray
        return outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
    }
}
