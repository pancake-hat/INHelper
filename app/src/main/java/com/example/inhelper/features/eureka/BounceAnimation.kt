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
class BounceAnimation(
    private val scope: CoroutineScope,
    private val bounceOffset: Animatable<Float, AnimationVector1D>
) {
    val modifier: Modifier
        get() = Modifier.offset(x = bounceOffset.value.dp)

    fun applyBounceAnimation() {
        scope.launch {
            repeat(3) {
                bounceOffset.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(durationMillis = 50)
                )
                bounceOffset.animateTo(
                    targetValue = -10f,
                    animationSpec = tween(durationMillis = 50)
                )
            }
            bounceOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 50)
            )
        }
    }
}
