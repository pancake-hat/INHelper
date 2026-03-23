package com.example.inhelper.features.eureka.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.inhelper.features.eureka.ShakeAnimation
import com.example.inhelper.features.eureka.EurekaSortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EurekaScreenTopBar(
    totalObtained: Int,
    maxObtained: Int,
    isEurekaLocked: Boolean,
    onLockToggled: (Boolean) -> Unit,
    sortType: EurekaSortType,
    onSortSelected: (EurekaSortType) -> Unit,
    onImportClicked: () -> Unit,
    onOpenDrawer: () -> Unit,
    animationController: ShakeAnimation
) {
    TopAppBar(
        title = {
            Text(text = "Eureka Tracker: $totalObtained/$maxObtained")
        },
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            LockEurekaAction(
                isLocked = isEurekaLocked,
                onToggle = onLockToggled,
                modifier = animationController.modifier
            )
            EurekaSettingsMenuAction(
                sortType = sortType,
                onSortSelected = onSortSelected,
                onImportClicked = onImportClicked
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun LockEurekaAction(
    isLocked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            val newLockedState = !isLocked
            onToggle(newLockedState)
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
            contentDescription = if (isLocked) "Unlock Obtained Eureka" else "Lock Obtained Eureka"
        )
    }
}

@Composable
private fun EurekaSettingsMenuAction(
    sortType: EurekaSortType,
    onSortSelected: (EurekaSortType) -> Unit,
    onImportClicked: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Settings"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sort A-Z") },
                onClick = {
                    onSortSelected(EurekaSortType.ALPHABETICAL)
                    expanded = false
                },
                leadingIcon = {
                    if (sortType == EurekaSortType.ALPHABETICAL) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            DropdownMenuItem(
                text = { Text("Sort by completion") },
                onClick = {
                    onSortSelected(EurekaSortType.OBTAINED)
                    expanded = false
                },
                leadingIcon = {
                    if (sortType == EurekaSortType.OBTAINED) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Import Eureka CSV") },
                onClick = {
                    onImportClicked()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}
