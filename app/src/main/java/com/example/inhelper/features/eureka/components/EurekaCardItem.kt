package com.example.inhelper.features.eureka.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaInfo
import com.example.inhelper.features.eureka.domain.model.EurekaType
import com.example.inhelper.utils.getEurekaPainter

@Composable
fun EurekaCardItem(
    type: EurekaType,
    setObtained: EurekaObtained,
    setInfo: EurekaInfo,
    onEurekaSetChange: (EurekaObtained) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemObtainedArray = when (type) {
        EurekaType.HEAD -> setObtained.headObtained
        EurekaType.HAND -> setObtained.handsObtained
        EurekaType.FEET -> setObtained.feetObtained
    }

    val resName = when (type) {
        EurekaType.HEAD -> setInfo.headRes
        EurekaType.HAND -> setInfo.handsRes
        EurekaType.FEET -> setInfo.feetRes
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(56.dp),
            painter = getEurekaPainter(resName, type),
            contentDescription = null
        )
        ColorCheckboxListView(
            setInfo = setInfo,
            isObtained = itemObtainedArray.toTypedArray(),
            onCheckedChange = { index, checked ->
                val updatedItem = itemObtainedArray.copyOf()
                updatedItem[index] = checked

                when (type) {
                    EurekaType.HEAD -> onEurekaSetChange(setObtained.copy(headObtained = updatedItem))
                    EurekaType.HAND -> onEurekaSetChange(setObtained.copy(handsObtained = updatedItem))
                    EurekaType.FEET -> onEurekaSetChange(setObtained.copy(feetObtained = updatedItem))
                }
            }
        )
    }
}
