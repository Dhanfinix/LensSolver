package com.dhandev.lenssolver

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhandev.lenssolver.ui.theme.Pink40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LensDialog(
    modifier: Modifier = Modifier,
    onDismiss: ()->Unit,
    onCamera: ()->Unit,
    onPicker: ()->Unit
) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        val btnModifier = Modifier.fillMaxWidth()
        val btnColor = ButtonDefaults.buttonColors(
            containerColor = Pink40
        )
        val cornerDp = 8.dp
        Card{
            Column(modifier.padding(16.dp)) {
                Button(
                    modifier = btnModifier,
                    colors = btnColor,
                    shape = RoundedCornerShape(topStart = cornerDp, topEnd = cornerDp),
                    onClick = {
                        onDismiss()
                        onPicker()
                    }
                ) {
                    Text(text = "Pick from Gallery")
                }
                Button(
                    modifier = btnModifier,
                    colors = btnColor,
                    shape = RoundedCornerShape(bottomStart = cornerDp, bottomEnd = cornerDp),
                    onClick = {
                        onDismiss()
                        onCamera()
                    }
                ) {
                    Text(text = "Take a Picture")
                }
            }
        }
    }
}