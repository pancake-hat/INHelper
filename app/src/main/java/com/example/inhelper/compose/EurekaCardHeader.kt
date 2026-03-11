package com.example.inhelper.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhelper.utils.formatEurekaSetName
import com.example.inhelper.utils.getEurekaPainter

@Composable
fun EurekaCardHeader(
    eurekaSetName: String,
    obtainedCount: Int,
    expanded: Boolean,
    headRes: String?,
    onExpandClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExpandClicked() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!expanded) {
            Image(
                painter = getEurekaPainter(headRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 4.dp)
            )
        }
        Text(
            text = formatEurekaSetName(eurekaSetName),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EurekaProgressView(
                obtainedCount = obtainedCount
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.padding(start = 8.dp, end = 0.dp)
            )
        }
    }
}
