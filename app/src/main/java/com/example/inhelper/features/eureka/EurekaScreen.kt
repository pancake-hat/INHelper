package com.example.inhelper.features.eureka

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhelper.data.local.entities.EurekaSet
import com.example.inhelper.features.eureka.components.EurekaListView
import com.example.inhelper.features.eureka.components.EurekaScreenTopBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EurekaScreen(
    modifier: Modifier = Modifier,
    viewModel: EurekaViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val eurekaList: List<EurekaSet> by viewModel.eurekaList.collectAsStateWithLifecycle()
    val sortType: EurekaSortType by viewModel.sortType.collectAsStateWithLifecycle()
    val isEurekaLocked: Boolean by viewModel.isEurekaLocked.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val bounceOffset = remember { Animatable(0f) }

    val animationController = remember(scope, bounceOffset) {
        BounceAnimation(scope, bounceOffset)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is EurekaUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importFromUri(it, context) }
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
                onImportClicked = { importLauncher.launch("text/comma-separated-values") },
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
