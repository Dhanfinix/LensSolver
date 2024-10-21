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
import androidx.compose.ui.unit.dp


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
        Card{
            Column(modifier.padding(16.dp)) {
                Button(
                    modifier = btnModifier,
                    onClick = {
                        onDismiss()
                        onPicker()
                    }
                ) {
                    Text(text = "Pick from Gallery")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    modifier = btnModifier,
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