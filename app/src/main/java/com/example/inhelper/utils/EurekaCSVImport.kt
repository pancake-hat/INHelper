package com.example.inhelper.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.inhelper.data.local.EurekaRegistry
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.reflect.Type

object EurekaCSVImport {
    const val TAG = "ConvertCSV"
    const val IMPORTED_EUREKA_FILE_NAME = "eureka_sets_imported.json"
    const val EUREKA_TEMPLATE_JSON_FILE_NAME = "eureka_sets.json"


    private class EurekaSetSerializer(private val registry: EurekaRegistry) : JsonSerializer<EurekaObtained> {
        override fun serialize(src: EurekaObtained, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            val jsonObject = JsonObject()
            val setInfo = registry.getEurekaSetInfo(src.setName)

            jsonObject.addProperty("setName", src.setName.name)

            jsonObject.add("color1", context.serialize(setInfo.color1))
            jsonObject.add("color2", context.serialize(setInfo.color2))
            jsonObject.add("color3", context.serialize(setInfo.color3))
            jsonObject.add("color4", context.serialize(setInfo.color4))
            jsonObject.add("color5", context.serialize(setInfo.color5))

            jsonObject.addProperty("headRes", setInfo.headRes)
            jsonObject.addProperty("handsRes", setInfo.handsRes)
            jsonObject.addProperty("feetRes", setInfo.feetRes)

            jsonObject.add("headObtained", context.serialize(src.headObtained))
            jsonObject.add("handsObtained", context.serialize(src.handsObtained))
            jsonObject.add("feetObtained", context.serialize(src.feetObtained))

            return jsonObject
        }
    }

    fun createImportedJsonFile(
        context: Context,
        csvUri: Uri,
        registry: EurekaRegistry,
        outputFileName: String = IMPORTED_EUREKA_FILE_NAME
    ): String? {
        val jsonString = convertEurekaCsvToJson(context, csvUri, registry)
        
        return try {
            val file = File(context.filesDir, outputFileName)
            
            // Replace imported json file if exists
            if (file.exists()) {
                Log.d(TAG, "Deleting existing file: ${file.absolutePath}")
                file.delete()
            }

            file.writeText(jsonString)
            Log.d(TAG, "Successfully created new JSON copy at: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write new JSON file: ${e.message}")
            null
        }
    }

    fun readImportedJsonFile(
        context: Context,
        fileName: String = IMPORTED_EUREKA_FILE_NAME
    ): List<EurekaObtained>? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return null

            val jsonString = file.readText()
            val gson = Gson()
            val type = object : TypeToken<List<EurekaObtained>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read JSON file: ${e.message}")
            null
        }
    }

    private fun convertEurekaCsvToJson(
        context: Context,
        csvUri: Uri,
        registry: EurekaRegistry
    ): String {
        // Force json field order to match EurekaSetObtained
        val gson = GsonBuilder()
            .registerTypeAdapter(EurekaObtained::class.java, EurekaSetSerializer(registry))
            .setPrettyPrinting()
            .create()
            
        val eurekaMap = mutableMapOf<EurekaSetName, EurekaObtained>()
        val originalOrder = mutableListOf<EurekaSetName>()

        // Load template JSON (read-only)
        try {
            context.assets.open(EUREKA_TEMPLATE_JSON_FILE_NAME).use { inputStream ->
                val reader = InputStreamReader(inputStream)
                val type = object : TypeToken<List<EurekaObtained>>() {}.type
                val existingSets: List<EurekaObtained> = gson.fromJson(reader, type)
                existingSets.forEach { 
                    eurekaMap[it.setName] = it
                    originalOrder.add(it.setName)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not load base JSON: ${e.message}")
        }

        // todo: does not check for csv contents, we are assuming it is exported as csv from:
        // https://docs.google.com/spreadsheets/d/1Ak0ezNY42eLiKwCsbqOmSxLYlbSGYAP9W2umM_v9rP4/edit?gid=1116526076
        // eg. exported csv will look like example_eureka_colors_csv.csv
        try { // Parse csv from uri and create updated copies of EurekaSet
            context.contentResolver.openInputStream(csvUri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readLine() // Skip header

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        if (line.isNullOrBlank() || line.startsWith(",")) continue

                        val tokens = line.split(",")
                        if (tokens.size < 10) continue

                        val rawName = tokens[0].trim() // setName
                        val category = tokens[1].trim()

                        val enumName = rawName.uppercase().replace(" ", "_")
                        val setName = try {
                            EurekaSetName.valueOf(enumName)
                        } catch (e: Exception) {
                            null
                        } ?: continue

                        val currentSet = eurekaMap[setName] ?: continue

                        // Map CSV columns 5-9 (colors) to a new BooleanArray
                        val obtained = BooleanArray(5) { i ->
                            tokens[5 + i].trim().equals("TRUE", ignoreCase = true)
                        }

                        val updatedSet = when (category) {
                            "Head" -> currentSet.copy(headObtained = obtained)
                            "Hands" -> currentSet.copy(handsObtained = obtained)
                            "Feet" -> currentSet.copy(feetObtained = obtained)
                            else -> currentSet
                        }
                        
                        eurekaMap[setName] = updatedSet
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing CSV: ${e.message}")
        }

        // Return updated list
        val orderedList = originalOrder.mapNotNull { eurekaMap[it] }
        return gson.toJson(orderedList)
    }
}
