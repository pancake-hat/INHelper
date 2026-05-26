package com.example.inhelper.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.inhelper.R
import com.example.inhelper.data.local.EurekaTypeConverters
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaColor
import com.example.inhelper.features.eureka.domain.model.EurekaInfo
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import com.example.inhelper.features.eureka.domain.model.EurekaSet
import com.example.inhelper.features.eureka.domain.model.EurekaType

@Composable
fun getEurekaPainter(resName: String?, type: EurekaType? = null): Painter {
    val context = LocalContext.current
    val imageRes = EurekaTypeConverters.getDrawableRes(context, resName)
    return if (imageRes != 0) {
        painterResource(id = imageRes)
    } else when(type) {
        EurekaType.HEAD -> painterResource(id = R.drawable.icon_eureka_head)
        EurekaType.HAND -> painterResource(id = R.drawable.icon_eureka_hands)
        EurekaType.FEET -> painterResource(id = R.drawable.icon_eureka_feet)
        else -> painterResource(id = R.drawable.ic_launcher_foreground)
    }
}

fun formatEurekaSetName(name: String): String {
    return name.split("_").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }
}

fun getExampleEurekaSetObtained(
    setName: EurekaSetName = EurekaSetName.SHROOMLING_LULLABY,
): EurekaObtained {
    return EurekaObtained(
        setName = setName,
        headObtained = booleanArrayOf(true, true, false, true, true),
        handsObtained = booleanArrayOf(true, true, false, true, true),
        feetObtained = booleanArrayOf(true, false, true, false, true)
    )
}

fun getExampleEurekaSetInfo(
    setName: EurekaSetName = EurekaSetName.SHROOMLING_LULLABY,
): EurekaInfo {
    return EurekaInfo(
        setName = setName,
        color1 = EurekaColor.RED,
        color2 = EurekaColor.BLUE,
        color3 = EurekaColor.YELLOW,
        color4 = EurekaColor.GREEN,
        color5 = EurekaColor.IRIDESCENT,
        headRes = "icon_eureka_head",
        handsRes = "icon_eureka_hands",
        feetRes = "icon_eureka_feet"
    )
}

fun getExampleEurekaSet(
    setName: EurekaSetName = EurekaSetName.SHROOMLING_LULLABY,
): EurekaSet {
    return EurekaSet(
        obtained = getExampleEurekaSetObtained(setName),
        info = getExampleEurekaSetInfo(setName)
    )
}
