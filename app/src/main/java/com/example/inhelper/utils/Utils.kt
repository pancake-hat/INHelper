package com.example.inhelper.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.inhelper.R
import com.example.inhelper.data.EurekaSet
import com.example.inhelper.data.EurekaTypeConverters

@Composable
fun getEurekaPainter(resName: String?): Painter {
    val context = LocalContext.current
    val imageRes = EurekaTypeConverters.getDrawableRes(context, resName)
    return if (imageRes != 0) {
        painterResource(id = imageRes)
    } else {
        painterResource(id = R.drawable.ic_launcher_foreground)
    }
}

fun formatEurekaSetName(name: String): String {
    return name.split("_").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun getPreviewEureka(
    setName: EurekaSetName = EurekaSetName.SHROOMLING_LULLABY,
    color1: EurekaColor = EurekaColor.RED,
    color2: EurekaColor = EurekaColor.BLUE,
    color3: EurekaColor = EurekaColor.YELLOW,
    color4: EurekaColor = EurekaColor.GREEN,
    color5: EurekaColor = EurekaColor.PURPLE,
): EurekaSet {
    val eureka = EurekaSet(
        setName = setName,
        color1 = color1,
        color2 = color2,
        color3 = color3,
        color4 = color4,
        color5 = color5,
        headRes = null,
        handsRes = null,
        feetRes = null,
        headObtained = booleanArrayOf(true, true, false, true, true),
        handsObtained = booleanArrayOf(true, true, false, true, true),
        feetObtained = booleanArrayOf(true, false, true, false, true)
    )

    return eureka
}