package com.famas.glassmorphismcompose

import android.graphics.*
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.random.Random

@Composable
fun GlassmorphismSphere() {
    var glassPosition by remember { mutableStateOf(Offset.Zero) }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                glassPosition += dragAmount
            }
        }) {
        val radius = min(size.width, size.height) / 5f
        val center = Offset(size.width / 2f, size.height / 2f)
        val gradientShader = LinearGradientShader(
            from = center + Offset(-radius, -radius),
            to = center + Offset(radius, radius),
            colors = listOf(
                Color.Magenta,
                Color.Cyan
            )
        )

        val glassPath = Path().apply {
            addArc(Rect(center, radius), 0f, 360f)
        }

        // Draw sphere
        drawCircle(
            brush = ShaderBrush(gradientShader),
            radius = radius,
            center = center
        )

        drawGlass(center, radius, glassPosition)
    }

}


fun DrawScope.drawGlass(center: Offset, radius: Float, position: Offset) {
    val glassRadius = radius
    val glassCenter = center + position
    val glassShader = LinearGradientShader(
        from = glassCenter + Offset(-glassRadius, -glassRadius),
        to = glassCenter + Offset(glassRadius, glassRadius),
        colors = listOf(
            Color.White.copy(alpha = 0.1f),
            Color.White.copy(alpha = 0.4f),
            Color.White.copy(alpha = 0.4f),
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.1f)
        ),
        colorStops = listOf(0f, 0.2f, 0.5f, 0.8f, 1f)
    )


    // Draw blurred circle
    drawIntoCanvas { canvas ->
        val paint = Paint()
        paint.asFrameworkPaint().maskFilter = BlurMaskFilter(7f, BlurMaskFilter.Blur.INNER)
        canvas.drawCircle(glassCenter, glassRadius, paint.apply {
            shader = glassShader
        })
    }
}


@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) {
        this@toDp.toDp()
    }
}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) {
        this@toPx.toPx()
    }
}