package com.example.inhelper.features.eureka.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.inhelper.features.eureka.domain.model.EurekaInfo

@Composable
fun ColorCheckboxListView(
    setInfo: EurekaInfo,
    modifier: Modifier = Modifier,
    isObtained: Array<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ColorCheckbox(modifier = modifier, eurekaColor = setInfo.color1, checked = isObtained[0], onCheckedChange = { onCheckedChange(0, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = setInfo.color2, checked = isObtained[1], onCheckedChange = { onCheckedChange(1, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = setInfo.color3, checked = isObtained[2], onCheckedChange = { onCheckedChange(2, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = setInfo.color4, checked = isObtained[3], onCheckedChange = { onCheckedChange(3, it) })
        ColorCheckbox(modifier = modifier, eurekaColor = setInfo.color5, checked = isObtained[4], onCheckedChange = { onCheckedChange(4, it) })
    }
}
