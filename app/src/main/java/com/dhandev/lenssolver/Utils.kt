package com.dhandev.lenssolver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream

object Utils {
    fun decodeUriToBitmap(context: Context, imageUri: Uri?): Bitmap? {
        if (imageUri == null){
            return null
        }
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}