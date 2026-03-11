package com.example.inhelper.compose

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.inhelper.utils.EurekaCSVImport
import com.example.inhelper.viewmodels.EurekaListViewModel
import com.example.inhelper.viewmodels.EurekaSortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EurekaScreenTopBar(
    totalObtained: Int,
    maxObtained: Int,
    isEurekaLocked: Boolean,
    onLockToggled: (Boolean) -> Unit,
    sortType: EurekaSortType,
    onSortSelected: (EurekaSortType) -> Unit,
    animationController: BounceAnimation
) { 
    TopAppBar(
        title = {
            Text(text = "Eureka Tracker: $totalObtained/$maxObtained")
        },
        actions = {
            ImportEurekaAction()
            LockEurekaAction(
                isLocked = isEurekaLocked,
                onToggle = onLockToggled,
                modifier = animationController.modifier
            )
            SortMenuAction(
                sortType = sortType,
                onSortSelected = onSortSelected
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun ImportEurekaAction() {
    var expanded by remember { mutableStateOf(false) }
    val onImportClick = tryImportEurekaCSV()
    
    Box {
        IconButton(onClick = {expanded = !expanded}) {
            Icon(
                imageVector = Icons.Default.UploadFile,
                contentDescription = "Import CSV"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Import Eureka CSV") },
                onClick = {
                    onImportClick()
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

@Composable
private fun tryImportEurekaCSV(): () -> Unit {
    val context = LocalContext.current
    val viewModel: EurekaListViewModel = hiltViewModel()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val result = EurekaCSVImport.createImportedJsonFile(context, uri)
            if (result != null) {
                Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
                viewModel.importEurekaSets(context)
            }
        } else {
            Toast.makeText(context, "Import failed", Toast.LENGTH_SHORT).show()
        }
    }

    return {
        launcher.launch("text/comma-separated-values")
    }
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
private fun SortMenuAction(
    sortType: EurekaSortType,
    onSortSelected: (EurekaSortType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort options"
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
        }
    }
}
