package edts.tms.feature_scanner.qr_screen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CameraOverlayComp(
    modifier: Modifier = Modifier,
    clearWidth: Dp = 240.dp,
    clearHeight: Dp = 240.dp,
    clearRadius: Dp = 8.dp,
    dimColor: Color = Color.Black.copy(alpha = 0.5f),
    content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(dimColor) // Dimmed background
    ) {
        // Clear part
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val clearWidthPx = clearWidth.toPx()// Width of the clear area
            val clearHeightPx = clearHeight.toPx() // Height of the clear area

            val topLeftX = (canvasWidth - clearWidthPx) / 2
            val topLeftY = (canvasHeight - clearHeightPx) / 2.5f

            drawRoundRect(
                color = Color.Transparent,
                topLeft = Offset(x = topLeftX, y = topLeftY),
                size = Size(width = clearWidthPx, height = clearHeightPx),
                blendMode = BlendMode.Clear,
                cornerRadius = CornerRadius(clearRadius.toPx())
            )
        }
        content()
    }
}