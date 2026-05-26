package com.example.inhelper.features.eureka.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.EurekaSortType
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import com.example.inhelper.features.eureka.domain.model.EurekaSet
import com.example.inhelper.ui.theme.INHelperTheme
import com.example.inhelper.utils.getExampleEurekaSet

@Composable
fun EurekaListView(
    list: List<EurekaSet>,
    sortType: EurekaSortType,
    modifier: Modifier = Modifier,
    onEurekaSetChange: (EurekaObtained) -> Unit = {}
) {
    val listState = rememberLazyListState()

    LaunchedEffect(sortType) {
        listState.scrollToItem(0)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .padding(top = 4.dp)
    ) {
        items(
            items = list,
            key = { it.info.setName.name }
        ) { item ->
            EurekaCardView(
                setObtained = item.obtained,
                setInfo = item.info,
                onEurekaSetChange = onEurekaSetChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EurekaListViewPreview() {
    val testItem1 = getExampleEurekaSet()
    val testItem2 = getExampleEurekaSet(setName = EurekaSetName.ROSEHEART)
    val testItem3 = getExampleEurekaSet(setName = EurekaSetName.RAYPLUME)

    val eurekaList = listOf(testItem1, testItem2, testItem3)

    INHelperTheme {
        EurekaListView(eurekaList, EurekaSortType.ALPHABETICAL)
    }
}
