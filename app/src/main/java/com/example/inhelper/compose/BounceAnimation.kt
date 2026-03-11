package com.example.inhelper.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val BOUNCE_ANIMATION_DURATION = 100
const val BOUNCE_REPEAT_COUNT = 3
const val BOUNCE_MIN_OFFSET = -5f
const val BOUNCE_MAX_OFFSET = 0f

class BounceAnimation(
    private val scope: CoroutineScope,
    private val bounceOffset: Animatable<Float, AnimationVector1D>
) {
    val modifier: Modifier
        get() = Modifier.offset(
            x = 0.dp,
            y = bounceOffset.value.dp
        )

    fun applyBounceAnimation() {
        scope.launch {
            repeat(BOUNCE_REPEAT_COUNT) {
                bounceOffset.animateTo(
                    targetValue = BOUNCE_MIN_OFFSET,
                    animationSpec = tween(durationMillis = BOUNCE_ANIMATION_DURATION)
                )
                bounceOffset.animateTo(
                    targetValue = BOUNCE_MAX_OFFSET,
                    animationSpec = tween(durationMillis = BOUNCE_ANIMATION_DURATION)
                )
            }
        }
    }
}
