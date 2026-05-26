package com.example.inhelper.features.eureka.domain.model

data class EurekaInfo(
    val setName: EurekaSetName,

    val color1: EurekaColor,
    val color2: EurekaColor,
    val color3: EurekaColor,
    val color4: EurekaColor,
    val color5: EurekaColor = EurekaColor.IRIDESCENT,

    val headRes: String? = null,
    val handsRes: String? = null,
    val feetRes: String? = null,
    val version: Float = 1.0f
)
