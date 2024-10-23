package com.dhandev.lenssolver

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhandev.lenssolver.component.BulletListComp
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
            Column(Modifier.padding(16.dp)) {
                Text(
                    modifier = btnModifier,
                    text = "Informasi",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Pink40
                )
                Spacer(modifier = Modifier.height(4.dp))
                BulletListComp(
                    btnModifier,
                    items = listOf(
                        "Gunakan gambar yang jelas tulisannya",
                        "Lebih disarankan ambil gambar dari Galeri",
                        "Gambar hanya menampilkan satu soal",
                        "Pastikan internet stabil",
                        "Jangan spam \"Solve\" dalam waktu berdekatan"
                    ),
                    style = TextStyle(),
                    lineSpacing = 2.dp
                )
            }
        }
    }
}