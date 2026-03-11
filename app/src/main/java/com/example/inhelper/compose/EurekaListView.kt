package com.example.inhelper.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.utils.EurekaSetName
import com.example.inhelper.ui.theme.INHelperTheme
import com.example.inhelper.utils.getPreviewEureka
import com.example.inhelper.viewmodels.EurekaSortType

@Composable
fun EurekaListView(
    list: List<EurekaSet>,
    sortType: EurekaSortType,
    modifier: Modifier = Modifier,
    onEurekaSetChange: (EurekaSet) -> Unit = {}
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
            key = { it.setName.name }
        ) { eureka ->
            EurekaCardView(
                eurekaSet = eureka,
                onEurekaSetChange = onEurekaSetChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EurekaListViewPreview() {
    val testSet1 = getPreviewEureka()
    val testSet2 = getPreviewEureka(setName = EurekaSetName.ROSEHEART)
    val testSet3 = getPreviewEureka(setName = EurekaSetName.RAYPLUME)

    val eurekaList = listOf(testSet1, testSet2, testSet3)

    INHelperTheme {
        EurekaListView(eurekaList, EurekaSortType.ALPHABETICAL)
    }
}
