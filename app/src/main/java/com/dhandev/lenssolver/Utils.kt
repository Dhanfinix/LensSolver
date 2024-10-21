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
            "Format jawaban yang benar:\n" +
            "Gunakan bungkus teks dengan double asterisks untuk menandai teks yang dianggap penting (misalnya rumus atau hasil akhir).\n" +
            "Gunakan single asterisk dengan spasi diawal kalimat untuk membuat poin-poin penting dalam penjelasan (contoh: * ini merupakan awal kalimat).\n" +
            "Gunakan single asterisk di awal dan akhir variabel yang digunakan dalam rumus (contoh: *v* atau *g*).\n" +
            "Gunakan unicode untuk superscript dan subscript, jika tidak ada yang cocok Gunakan tag <sub> untuk subscript dan tag <sup>** untuk superscript, jika diperlukan (contoh: H<sub>2</sub>O atau x<sup>2</sup>).\n" +
            "Jika langkah-langkah atau format tersebut tidak bisa dilakukan, gunakan format apa saja" +
            "Jika tidak ada soal terdeteksi, jawab dengan: Soal tidak ditemukan, coba lagi dengan gambar lain ya."
    const val IMAGE_IDENTIFY_PROMPT = "Jelaskan gambar ini"
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

            // Updated regex to handle bullet points and italic text inside parentheses or within text
            val regex = "(\\*\\*(.*?)\\*\\*)|(\\*(.*?)\\*)|(<sub>(.*?)</sub>)|(<sup>(.*?)</sup>)|(^\\*\\s(.*?)(?=$|\\n))|\\(\\*(.*?)\\*\\)".toRegex()

            regex.findAll(text).forEach { matchResult ->
                val startIndex = matchResult.range.first
                val endIndex = matchResult.range.last

                // Append the text before the match
                append(text.substring(currentIndex, startIndex))

                when {
                    // Handle bold text
                    matchResult.groupValues[2].isNotEmpty() -> {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(matchResult.groupValues[2])
                        }
                    }
                    // Handle italic text (including text like *m* within bullet points)
                    matchResult.groupValues[4].isNotEmpty() -> {
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(matchResult.groupValues[4])
                        }
                    }
                    // Handle subscript text
                    matchResult.groupValues[6].isNotEmpty() -> {
                        withStyle(style = SpanStyle(fontSize = 12.sp, baselineShift = BaselineShift.Subscript)) {
                            append(matchResult.groupValues[6])
                        }
                    }
                    // Handle superscript text
                    matchResult.groupValues[8].isNotEmpty() -> {
                        withStyle(style = SpanStyle(fontSize = 12.sp, baselineShift = BaselineShift.Superscript)) {
                            append(matchResult.groupValues[8])
                        }
                    }
                    // Handle bullet points
                    matchResult.groupValues[10].isNotEmpty() -> {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {
                            append("• ") // Bullet symbol
                            append(matchResult.groupValues[10])
                        }
                    }
                    // Handle text inside parentheses with asterisks (only apply italic to text inside parentheses)
//                    matchResult.groupValues[12].isNotEmpty() -> {
//                        append("(") // Add opening parentheses
//                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
//                            append(matchResult.groupValues[12]) // Only apply italic to text inside asterisks
//                        }
//                        append(")") // Add closing parentheses
//                    }
                }

                // Update the current index
                currentIndex = endIndex + 1
            }

            // Append the remaining text after the last match
            append(text.substring(currentIndex))
        }

}