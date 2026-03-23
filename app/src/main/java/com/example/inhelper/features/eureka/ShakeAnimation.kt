package com.example.inhelper.features.eureka

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
class ShakeAnimation(
    private val scope: CoroutineScope,
    private val offset: Animatable<Float, AnimationVector1D>
) {
    val modifier: Modifier
        get() = Modifier.offset(x = offset.value.dp)

    fun applyShakeAnimation() {
        scope.launch {
            repeat(3) {
                offset.animateTo(
                    targetValue = 3f,
                    animationSpec = tween(durationMillis = 50)
                )
                offset.animateTo(
                    targetValue = -3f,
                    animationSpec = tween(durationMillis = 50)
                )
            }
            offset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 50)
            )
        }
    }
}
