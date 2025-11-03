package com.example.proyectologin005d.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ProductImage(
    nombreDrawable: String?,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    val id = nombreDrawable?.let {
        ctx.resources.getIdentifier(it, "drawable", ctx.packageName)
    } ?: 0

    if (id != 0) {
        Image(
            painter = painterResource(id),
            contentDescription = nombreDrawable,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(8.dp))
        )
    }
}
