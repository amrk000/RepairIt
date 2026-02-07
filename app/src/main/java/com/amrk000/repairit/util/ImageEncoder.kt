package com.amrk000.repairit.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import java.io.IOException

object ImageEncoder {

    // My Encoder Config
    private const val centerCrop = true
    private const val imageWidth = 500
    private const val imageHeight = 500
    private const val imageQuality = 100
    private val imageFormat = Bitmap.CompressFormat.WEBP
    private const val metaData = "data:image/webp;base64,"


    fun encode(inputBitmap: Bitmap): String {
        var bitmap: Bitmap = inputBitmap

        val byteArrayOutputStream = ByteArrayOutputStream()

        if (centerCrop) {
            bitmap = if (bitmap.width > bitmap.height)
                Bitmap.createBitmap(bitmap, bitmap.width / 2 - bitmap.height / 2, 0, bitmap.height, bitmap.height)
            else
                Bitmap.createBitmap(bitmap, 0, bitmap.height / 2 - bitmap.width / 2, bitmap.width, bitmap.width)
        }

        bitmap = bitmap.scale(imageWidth, imageHeight, false)

        bitmap.compress(imageFormat, imageQuality, byteArrayOutputStream)

        val image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)

        return metaData + image
    }

    fun decode(inputString: String): Bitmap? {
        val base64ImageString = if (inputString.startsWith(metaData)) {
            inputString.substring(metaData.length)
        } else inputString

        try {
            val decodedBytes = Base64.decode(base64ImageString, Base64.NO_WRAP)
            return BitmapFactory.decodeByteArray(
                decodedBytes,
                0,
                decodedBytes.size
            )
        }
        catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }
    }
}