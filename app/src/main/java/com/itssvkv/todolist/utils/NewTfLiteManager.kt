package com.itssvkv.todolist.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.itssvkv.todolist.utils.Constants.NUM_CLASSES
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

object NewTfLiteManager{
    private lateinit var tflite: Interpreter



    fun loadModel(context: Context) {
        try {
            val tfliteModel = FileUtil.loadMappedFile(context, "model.tflite")
            tflite = Interpreter(tfliteModel)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

     fun loadImageFromUri(uri: Uri, context: Context): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

     fun preprocessImage(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, Constants.MODEL_INPUT_WIDTH, Constants.MODEL_INPUT_HEIGHT, true)
    }

     fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * Constants.MODEL_INPUT_WIDTH * Constants.MODEL_INPUT_HEIGHT * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(Constants.MODEL_INPUT_WIDTH * Constants.MODEL_INPUT_HEIGHT)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until Constants.MODEL_INPUT_WIDTH) {
            for (j in 0 until Constants.MODEL_INPUT_HEIGHT) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(((`val` shr 16) and 0xFF) / 255.0f)
                byteBuffer.putFloat(((`val` shr 8) and 0xFF) / 255.0f)
                byteBuffer.putFloat((`val` and 0xFF) / 255.0f)
            }
        }
        return byteBuffer
    }

     fun runInference(byteBuffer: ByteBuffer): Int {
        val output = Array(1) { FloatArray(NUM_CLASSES) }
        tflite.run(byteBuffer, output)

        // Find the index of the highest probability
        var maxIdx = 0
        var maxProb = output[0][0]
        for (i in 1 until NUM_CLASSES) {
            if (output[0][i] > maxProb) {
                maxIdx = i
                maxProb = output[0][i]
            }
        }

        return maxIdx
    }

}