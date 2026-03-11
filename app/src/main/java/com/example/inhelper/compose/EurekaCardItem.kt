package com.example.inhelper.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.utils.EurekaType
import com.example.inhelper.utils.getEurekaPainter

@Composable
fun EurekaCardItem(
    type: EurekaType,
    eurekaSet: EurekaSet,
    onEurekaSetChange: (EurekaSet) -> Unit,
    modifier: Modifier
) {
    val itemObtainedArray = when (type) {
        EurekaType.HEAD -> eurekaSet.headObtained
        EurekaType.HAND -> eurekaSet.handsObtained
        EurekaType.FEET -> eurekaSet.feetObtained
    }

    val resName = when (type) {
        EurekaType.HEAD -> eurekaSet.headRes
        EurekaType.HAND -> eurekaSet.handsRes
        EurekaType.FEET -> eurekaSet.feetRes
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(56.dp),
            painter = getEurekaPainter(resName),
            contentDescription = null
        )
        ColorCheckboxListView(
            eureka = eurekaSet,
            isObtained = itemObtainedArray.toTypedArray(),
            onCheckedChange = { index, checked ->
                val updatedItem = itemObtainedArray.copyOf()
                updatedItem[index] = checked

                when (type) {
                    EurekaType.HEAD -> onEurekaSetChange(eurekaSet.copy(headObtained = updatedItem))
                    EurekaType.HAND -> onEurekaSetChange(eurekaSet.copy(handsObtained = updatedItem))
                    EurekaType.FEET -> onEurekaSetChange(eurekaSet.copy(feetObtained = updatedItem))
                }
            }
        )
    }
}
