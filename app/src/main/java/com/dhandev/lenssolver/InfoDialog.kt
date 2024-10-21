package com.dhandev.lenssolver

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhandev.lenssolver.ui.theme.Pink40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit
) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        val btnModifier = Modifier.fillMaxWidth()
        Card{
            Column(modifier.padding(16.dp)) {
                Text(
                    modifier = btnModifier,
                    text = "Informasi",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Pink40
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = btnModifier,
                    text = "Agar hasil lebih maksimal, perhatikan hal-hal berikut:" +
                            "\n• Gunakan gambar yang jelas tulisannya" +
                            "\n• Lebih disarankan ambil gambar dari Galeri" +
                            "\n• Gambar hanya menampilkan satu soal" +
                            "\n• Pastikan internet stabil" +
                            "\n• Jangan spam \"Solve\" dalam waktu berdekatan",
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}