package com.example.inhelper.features.eureka.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaInfo
import com.example.inhelper.features.eureka.domain.model.EurekaType
import com.example.inhelper.ui.theme.INHelperTheme
import com.example.inhelper.utils.getExampleEurekaSetObtained
import com.example.inhelper.utils.getExampleEurekaSetInfo

@Composable
fun EurekaCardView(
    setObtained: EurekaObtained,
    setInfo: EurekaInfo,
    modifier: Modifier = Modifier,
    onEurekaSetChange: (EurekaObtained) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column {
            EurekaCardHeader(
                eurekaSetName = setInfo.setName.toString(),
                obtainedCount = setObtained.totalObtainedCount(),
                expanded = expanded,
                headRes = setInfo.headRes,
                onExpandClicked = { expanded = !expanded }
            )

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EurekaCardItem(EurekaType.HEAD, setObtained, setInfo, onEurekaSetChange, modifier)
                    EurekaCardItem(EurekaType.HAND, setObtained, setInfo, onEurekaSetChange, modifier)
                    EurekaCardItem(EurekaType.FEET, setObtained, setInfo, onEurekaSetChange, modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EurekaCardViewPreview() {
    val testObtained = getExampleEurekaSetObtained()
    val testInfo = getExampleEurekaSetInfo()

    INHelperTheme {
        EurekaCardView(testObtained, testInfo)
    }
}
