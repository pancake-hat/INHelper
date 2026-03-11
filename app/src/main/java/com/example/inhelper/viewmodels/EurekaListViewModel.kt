package com.example.inhelper.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.data.EurekaSetRepository
import com.example.inhelper.utils.EurekaCSVImport
import com.example.inhelper.utils.MAX_EUREKA_COUNT_PER_SET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
class EurekaListViewModel @Inject internal constructor(
    private val eurekaSetRepository: EurekaSetRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _sortType = MutableStateFlow(EurekaSortType.ALPHABETICAL)
    val sortType: StateFlow<EurekaSortType> = _sortType

    // default should be locked
    private val _isEurekaLocked = MutableStateFlow(true)
    val isEurekaLocked: StateFlow<Boolean> = _isEurekaLocked

    val eurekaList: StateFlow<List<EurekaSet>> = combine(
        eurekaSetRepository.getEurekaSets(),
        _sortType
    ) { list, sort ->
        when (sort) {
            EurekaSortType.ALPHABETICAL -> list.sortedBy { it.setName.name }
            EurekaSortType.OBTAINED -> list.sortedByDescending { it.totalObtainedCount() }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateEurekaSet(eurekaSet: EurekaSet) {
        viewModelScope.launch {
            eurekaSetRepository.updateEurekaSet(eurekaSet)
        }
    }

    fun importEurekaSets(context: Context) {
        viewModelScope.launch {
            val importedSets = EurekaCSVImport.readImportedJsonFile(context)
            if (importedSets != null) {
                eurekaSetRepository.updateEurekaSets(importedSets)
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
