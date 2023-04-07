package com.omar.mentalcompanion.presentation.screens.questionnaire.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.omar.mentalcompanion.R

object CommonComponents {
    private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

    @Composable
    fun ResponsiveText(
        modifier: Modifier = Modifier,
        text: String,
        color: Color = colorResource(id = R.color.black),
        textAlign: TextAlign? = null,
        style: TextStyle,
        maxLines: Int = 1,
    ) {
        var textSize by remember { mutableStateOf(style.fontSize) }
        var lineHeight by remember { mutableStateOf(style.lineHeight) }

        Text(
            modifier = modifier,
            text = text,
            color = color,
            textAlign = textAlign,
            style = style.copy(fontSize = textSize, lineHeight = lineHeight),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    lineHeight = lineHeight.times(TEXT_SCALE_REDUCTION_INTERVAL)
                    textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
                }
            },
        )
    }
}

