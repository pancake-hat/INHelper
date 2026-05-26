package com.example.inhelper.data.local

import android.content.Context
import com.example.inhelper.features.eureka.domain.model.EurekaInfo
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import com.example.inhelper.utils.EUREKA_SETS_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EurekaRegistry @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var map: Map<EurekaSetName, EurekaInfo> = emptyMap()

    init {
        loadData()
    }

    private fun loadData() {
        if (map.isNotEmpty()) return
        
        try {
            context.assets.open(EUREKA_SETS_DATA_FILENAME).use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val type = object : TypeToken<List<EurekaInfo>>() {}.type
                val infoList: List<EurekaInfo> = Gson().fromJson(reader, type)
                map = infoList.associateBy { it.setName }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getEurekaSetInfo(setName: EurekaSetName): EurekaInfo {
        return map[setName] ?: throw IllegalStateException("Static info for $setName not found.")
    }
}
