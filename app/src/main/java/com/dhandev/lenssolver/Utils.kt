package com.dhandev.lenssolver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

    fun formatText(text: String): AnnotatedString =
        buildAnnotatedString {
            var currentIndex = 0
            val bulletRegex = "\\* (.*?)($|\n)".toRegex()
            val italicRegex = "\\*(.*?)\\*".toRegex()
            val boldRegex = "\\*\\*(.*?)\\*\\*".toRegex()

            // Handle bullet points first
            val bulletProcessedText = text.replace(bulletRegex) { matchResult ->
                "\u2022 ${matchResult.groupValues[1]}\n"
            }

            // Handle bold text
            boldRegex.findAll(bulletProcessedText).forEach { matchResult ->
                val startIndex = matchResult.range.first
                val endIndex = matchResult.range.last

                // Append the text before the match
                append(bulletProcessedText.substring(currentIndex, startIndex))

                // Apply the bold style
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(matchResult.groupValues[1])
                }

                // Update the current index
                currentIndex = endIndex + 1
            }

            currentIndex = 0

            italicRegex.findAll(bulletProcessedText).forEach { matchResult ->
                val startIndex = matchResult.range.first
                val endIndex = matchResult.range.last

                // Append the text before the match
                append(bulletProcessedText.substring(currentIndex, startIndex))

                // Apply the bold style
                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(matchResult.groupValues[1])
                }

                // Update the current index
                currentIndex = endIndex + 1
            }

            // Append the remaining text
            append(bulletProcessedText.substring(currentIndex))
        }
}