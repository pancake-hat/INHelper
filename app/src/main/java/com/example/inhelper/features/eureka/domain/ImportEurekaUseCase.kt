package com.example.inhelper.features.eureka.domain

import android.content.Context
import android.net.Uri
import com.example.inhelper.data.local.EurekaRegistry
import com.example.inhelper.data.repository.EurekaObtainedRepository
import com.example.inhelper.utils.EurekaCSVImport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImportEurekaUseCase @Inject constructor(
    private val repository: EurekaObtainedRepository,
    private val registry: EurekaRegistry
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val result = EurekaCSVImport.createImportedJsonFile(context, uri, registry)
            if (result != null) {
                val importedSets = EurekaCSVImport.readImportedJsonFile(context)
                if (importedSets != null) {
                    repository.updateAllEurekasObtained(importedSets)
                    return@withContext Result.success(Unit)
                }
            }
            Result.failure(Exception("Import failed: Could not process file"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
