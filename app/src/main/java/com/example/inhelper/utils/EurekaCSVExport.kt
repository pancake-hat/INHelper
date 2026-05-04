package com.example.inhelper.utils

import com.example.inhelper.data.local.entities.EurekaSet
import java.util.Locale

object EurekaCSVExport {

    private const val CSV_HEADER = "Name,Category,Trials,Style,Label,Colour 1,Colour 2,Colour 3,Colour 4,Iridescent,,,,,Name,Category,Colour,,Total Attempts,Iridescent"

    fun generateEurekaCsv(eurekaSets: List<EurekaSet>): String {
        val sb = StringBuilder()
        sb.append(CSV_HEADER).append("\n")

        eurekaSets.forEach { set ->
            sb.append(generateRow(set, "Head", set.headObtained))
            sb.append(generateRow(set, "Hands", set.handsObtained))
            sb.append(generateRow(set, "Feet", set.feetObtained))
        }

        return sb.toString()
    }

    private fun generateRow(set: EurekaSet, category: String, obtained: BooleanArray): String {
        val name = formatSetName(set.setName)
        val colors = obtained.joinToString(",") { it.toString().uppercase(Locale.ROOT) }

        return "$name,$category,,, ,$colors,,,,,,,,,,,\n"
    }

    private fun formatSetName(setName: EurekaSetName): String {
        return setName.name.lowercase(Locale.ROOT)
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString() } }
    }
}
