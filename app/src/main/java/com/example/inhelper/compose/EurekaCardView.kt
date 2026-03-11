package com.example.inhelper.compose

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
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.ui.theme.INHelperTheme
import com.example.inhelper.utils.EurekaType
import com.example.inhelper.utils.getPreviewEureka

@Composable
fun EurekaCardView(
    eurekaSet: EurekaSet,
    modifier: Modifier = Modifier,
    onEurekaSetChange: (EurekaSet) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column {
            EurekaCardHeader(
                eurekaSetName = eurekaSet.setName.toString(),
                obtainedCount = eurekaSet.totalObtainedCount(),
                expanded = expanded,
                headRes = eurekaSet.headRes,
                onExpandClicked = { expanded = !expanded }
            )

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EurekaCardItem(EurekaType.HEAD, eurekaSet, onEurekaSetChange, modifier)
                    EurekaCardItem(EurekaType.HAND, eurekaSet, onEurekaSetChange, modifier)
                    EurekaCardItem(EurekaType.FEET, eurekaSet, onEurekaSetChange, modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EurekaCardViewPreview() {
    val testSet = getPreviewEureka()

    INHelperTheme {
        EurekaCardView(testSet)
    }
}
