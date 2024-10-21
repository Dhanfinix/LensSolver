package com.dhandev.lenssolver.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CameraGridComp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            Spacer(modifier = Modifier.weight(1f))
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewCameraGrid() {
    CameraGridComp()
}