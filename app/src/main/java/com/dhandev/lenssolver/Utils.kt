package com.dhandev.lenssolver

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.io.InputStream

object Utils {
    const val PROMPT = "Kamu adalah ahli fisika dan matematika internasional.\n" +
            "\n" +
            "Selesaikan soal fisika atau matematika yang diberikan dengan benar dan tepat, disertai dengan langkah-langkah yang jelas, terstruktur, dan mudah dipahami.\n" +
            "Gunakan bahasa Indonesia dan tanggapi percakapan ini dengan cara yang ramah dan seperti seorang guru.\n" +
            "Langkah-langkah yang harus ada pada setiap penyelesaian soal:\n" +
            "Diketahui: Sebutkan informasi yang sudah diberikan pada soal.\n" +
            "Ditanyakan: Tentukan apa yang perlu dicari dari soal.\n" +
            "Penyelesaian: Jelaskan langkah-langkah penyelesaian dengan urut dan lengkap, sertakan rumus yang digunakan, dan pastikan solusi yang diberikan sesuai dengan soal.\n" +
            "Bungkus Jawaban tersebut dalam format HTML atau MathJax jika perlu" +
            "tidak perlu ada kata html diawal, tidak perlu tiga backticks, " +
            "gunakan <p> untuk setiap baris " +
            "gunakan <b> pada kata atau kalimat penting " +
            "gunakan <i> pada variable " +
            "gunakan <ul> dan <li> untuk bullet point " +
            "gunakan <sub> untuk subscript " +
            "gunakan <sup> untuk superscript " +
            "gunakan <b> untuk vektor " +
            "Jika tidak ada soal terdeteksi, jawab dengan: Soal tidak ditemukan, coba lagi dengan gambar lain ya."
    const val IMAGE_IDENTIFY_PROMPT = "Jelaskan gambar ini"
    fun resultHtmlWrapper(result: String) =
        """
            <html>
                <head>
                    <title>My HTML Page</title>
                    <style>
                        body {
                            background-color: transparent;
                            color: black; /* Set text color to ensure it's visible against a transparent background */
                            margin: 0; /* Remove default margin */
                            padding: 0; /* Remove default padding */
                            display: block; /* Ensure the body behaves like a block element */
                        }
                    </style>
                    <script src="https://polyfill.io/v3/polyfill.min.js?features=es6"></script>
                    <script type="text/javascript" id="MathJax-script" async
                      src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/3.0.0/es5/latest?tex-mml-chtml.js">
                    </script>
                </head>
                <body>
                    $result
                </body>
            </html>
        """
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