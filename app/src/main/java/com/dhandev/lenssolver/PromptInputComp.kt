package com.dhandev.lenssolver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhandev.lenssolver.ui.theme.Pink40

@Composable
fun PromptInputComp(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit // Callback for the changed value
) {
    var text by rememberSaveable(value) { mutableStateOf(value) }
    val textStyle = TextStyle(
        fontSize = 16.sp
    )
    Column {
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                onValueChanged(newValue) // Call the callback with the new value
            },
            modifier = modifier
                .fillMaxWidth()
                .border(2.dp, Pink40, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp)), // No border here
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.weight(1f)
                    ){
                        if(text.isEmpty()){
                            Text(
                                text = "Masukkan perintah kustom jika diperlukan",
                                maxLines = 3,
                                color = Color.Gray,
                                style = textStyle
                            )
                        }
                        innerTextField()
                    }
                    AnimatedVisibility(visible = text.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_clear_24),
                            colorFilter = ColorFilter.tint(Pink40),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                text = ""
                            }
                        )
                    }
                }

            }
        )

        AnimatedVisibility(visible = text.isNotEmpty()) {
            Text(
                text = "Perintah ini akan sepenuhnya mengganti perintah bawaan",
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
