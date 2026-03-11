package com.example.inhelper.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EurekaProgressView(
    modifier: Modifier = Modifier,
    obtainedCount: Int,
) {
    val maxCount = 15
    val progress = (obtainedCount.toFloat() / maxCount).coerceIn(0f, 1f)

    val startColor = Color(0xFFE57373)
    val endColor = Color(0xFF8AEC8F)
    val progressColor = lerp(startColor, endColor, progress)
    val trackColor = Color(0xFFA8A8A8)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$obtainedCount/$maxCount",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.width(64.dp),
            color = progressColor,
            trackColor = trackColor,
            gapSize = 0.dp,
            strokeCap = StrokeCap.Square,
            drawStopIndicator = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EurekaProgressViewPreview() {
    EurekaProgressView(obtainedCount = 7)
}
