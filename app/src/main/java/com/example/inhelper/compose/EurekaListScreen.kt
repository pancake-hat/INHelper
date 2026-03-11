package com.example.inhelper.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.viewmodels.EurekaListViewModel
import com.example.inhelper.viewmodels.EurekaSortType

@Composable
fun EurekaListScreen(
    modifier: Modifier = Modifier,
    viewModel: EurekaListViewModel = hiltViewModel(),
) {
    val eurekaList: List<EurekaSet> by viewModel.eurekaList.collectAsStateWithLifecycle()
    val sortType: EurekaSortType by viewModel.sortType.collectAsStateWithLifecycle()
    val isEurekaLocked: Boolean by viewModel.isEurekaLocked.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val bounceOffset = remember { Animatable(0f) }

    val animationController = remember(scope, bounceOffset) {
        BounceAnimation(scope, bounceOffset)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EurekaScreenTopBar(
                totalObtained = viewModel.getTotalEurekasObtained(eurekaList),
                maxObtained = viewModel.getMaxEurekaObtained(eurekaList),
                isEurekaLocked = isEurekaLocked,
                onLockToggled = { viewModel.setEurekaLocked(it) },
                sortType = sortType,
                onSortSelected = { viewModel.setSortType(it) },
                animationController = animationController
            )
        }
    ) { innerPadding ->
        if (eurekaList.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EurekaListView(
                list = eurekaList,
                sortType = sortType,
                modifier = modifier.padding(innerPadding),
                onEurekaSetChange = {
                    if (!isEurekaLocked) {
                        viewModel.updateEurekaSet(it)
                    } else { // bounce lock icon
                        animationController.applyBounceAnimation()
                    }
                }
            )
        }
    }
}
