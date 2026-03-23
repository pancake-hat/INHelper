package com.example.inhelper.features.eureka

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.inhelper.data.local.entities.EurekaSet
import com.example.inhelper.data.repository.EurekaSetRepository
import com.example.inhelper.features.eureka.domain.ImportEurekaUseCase
import com.example.inhelper.utils.MAX_EUREKA_COUNT_PER_SET
import com.example.inhelper.workers.SeedDatabaseWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class EurekaSortType {
    ALPHABETICAL,
    OBTAINED
}

@HiltViewModel
class EurekaViewModel @Inject internal constructor(
    private val eurekaSetRepository: EurekaSetRepository,
    private val importEurekaUseCase: ImportEurekaUseCase,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    companion object {
        private const val TAG = "EurekaViewModel"
    }
    private val _sortType = MutableStateFlow(EurekaSortType.ALPHABETICAL)
    val sortType: StateFlow<EurekaSortType> = _sortType

    // default should be locked
    private val _isEurekaLocked = MutableStateFlow(true)
    val isEurekaLocked: StateFlow<Boolean> = _isEurekaLocked

    private val _uiEvent = MutableSharedFlow<EurekaUiEvent>()
    val uiEvent: SharedFlow<EurekaUiEvent> = _uiEvent

    val eurekaList: StateFlow<List<EurekaSet>> = combine(
        eurekaSetRepository.getEurekaSets(),
        _sortType
    ) { list, sort ->
        if (list.isEmpty()) {
            checkAndSeedDatabase()
        }
        when (sort) {
            EurekaSortType.ALPHABETICAL -> list.sortedBy { it.setName.name }
            EurekaSortType.OBTAINED -> list.sortedByDescending { it.totalObtainedCount() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun checkAndSeedDatabase() {
        viewModelScope.launch {
            if (eurekaSetRepository.getEurekaSetsCount() == 0) {
                Log.d(TAG, "Database is empty, enqueuing seed worker")
                WorkManager.getInstance(context).enqueueUniqueWork(
                    SeedDatabaseWorker.SEED_DATABASE_WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    SeedDatabaseWorker.buildWorkRequest()
                )
            }
        }
    }

    fun updateEurekaSet(eurekaSet: EurekaSet) {
        viewModelScope.launch {
            eurekaSetRepository.updateEurekaSet(eurekaSet)
        }
    }

    fun importFromUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            val result = importEurekaUseCase(context, uri)
            if (result.isSuccess) {
                _uiEvent.emit(EurekaUiEvent.ShowToast("Import successful"))
            } else {
                _uiEvent.emit(EurekaUiEvent.ShowToast("Import failed: ${result.exceptionOrNull()?.message}"))
            }
        }
    }

    fun setSortType(sortType: EurekaSortType) {
        _sortType.value = sortType
    }

    fun setEurekaLocked(isLocked: Boolean) {
        _isEurekaLocked.value = isLocked
    }

    fun getTotalEurekasObtained(eurekaList: List<EurekaSet>): Int {
        return eurekaList.sumOf { it.totalObtainedCount() }
    }

    fun getMaxEurekaObtained(eurekaList: List<EurekaSet>): Int {
        return eurekaList.size * MAX_EUREKA_COUNT_PER_SET
    }
}

sealed class EurekaUiEvent {
    data class ShowToast(val message: String) : EurekaUiEvent()
}
