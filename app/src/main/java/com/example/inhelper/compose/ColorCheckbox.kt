package com.example.inhelper.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inhelper.utils.EurekaColor
import com.example.inhelper.R

@Composable
fun ColorCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    eurekaColor: EurekaColor,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val colorIcon = when (eurekaColor) {
        EurekaColor.WHITE -> R.drawable.eureka_color_icon_white
        EurekaColor.RED -> R.drawable.eureka_color_icon_red
        EurekaColor.PINK -> R.drawable.eureka_color_icon_pink
        EurekaColor.YELLOW -> R.drawable.eureka_color_icon_yellow
        EurekaColor.GREEN -> R.drawable.eureka_color_icon_green
        EurekaColor.BLUE -> R.drawable.eureka_color_icon_blue
        EurekaColor.PURPLE -> R.drawable.eureka_color_icon_purple
        EurekaColor.IRIDESCENT -> R.drawable.eureka_color_icon_iridescent
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = modifier,
            enabled = enabled,
            onClick = {
                onCheckedChange(!checked)
            }
        ) {
            Image( // background unchecked image
                modifier = Modifier
                    .size(36.dp),
                painter = painterResource(id = colorIcon),
                contentDescription = "Unchecked"
            )
            AnimatedVisibility(
                modifier = modifier,
                visible = checked,
                exit = shrinkOut(shrinkTowards = Alignment.TopStart) + fadeOut()
            ) {
                Icon( // check image
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Checked",
                    tint = Color.Black
                )
            }
        }
        Text(
            eurekaColor.toString().lowercase(),
            fontSize = 12.sp,
            style = if(checked) {
                TextStyle(textDecoration = TextDecoration.LineThrough)
            } else {
                TextStyle(textDecoration = TextDecoration.None)
            },
        )
    }
}
