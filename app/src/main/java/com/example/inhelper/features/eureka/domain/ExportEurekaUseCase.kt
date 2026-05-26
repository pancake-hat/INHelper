package com.example.inhelper.features.eureka.domain

import android.content.Context
import android.net.Uri
import com.example.inhelper.data.repository.EurekaObtainedRepository
import com.example.inhelper.utils.EurekaCSVExport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import javax.inject.Inject

class ExportEurekaUseCase @Inject constructor(
    private val repository: EurekaObtainedRepository
) {
    suspend operator fun invoke(context: Context, uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val eurekaSets = repository.getEurekasObtainedList().first()
            val csvString = EurekaCSVExport.generateEurekaCsv(eurekaSets)
            
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write(csvString)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
