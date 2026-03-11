package com.example.inhelper.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.inhelper.data.EurekaSet

@Composable
fun ColorCheckboxListView(
    eureka: EurekaSet,
    modifier: Modifier = Modifier,
    isObtained: Array<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ColorCheckbox(modifier = modifier, eurekaColor = eureka.color1, checked = isObtained[0], onCheckedChange = { onCheckedChange(0, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = eureka.color2, checked = isObtained[1], onCheckedChange = { onCheckedChange(1, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = eureka.color3, checked = isObtained[2], onCheckedChange = { onCheckedChange(2, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = eureka.color4, checked = isObtained[3], onCheckedChange = { onCheckedChange(3, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = eureka.color5, checked = isObtained[4], onCheckedChange = { onCheckedChange(4, it) })
    }
}
