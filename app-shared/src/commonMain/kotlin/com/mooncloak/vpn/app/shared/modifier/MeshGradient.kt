/**
 * The following code is adapted from the following project under the Apache 2.0 License:
 * https://github.com/c5inco/Mesh
 * https://github.com/c5inco/Mesh?tab=Apache-2.0-1-ov-file#readme
 *
 * Which in turn was inspired by the following gist:
 * https://gist.github.com/sinasamaki/05725557c945c5329fdba4a3494aaecb
 */

package com.mooncloak.vpn.app.shared.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Immutable
public data class Point public constructor(
    public val offset: Offset,
    public val color: Color
)

// Adapted from: https://gist.github.com/sinasamaki/05725557c945c5329fdba4a3494aaecb
@Composable
public fun Modifier.meshGradient(
    points: List<List<Point>>,
    blurLevel: Int = 0,
    gradientBlendMode: BlendMode = BlendMode.DstIn,
    resolutionX: Int = 1,
    resolutionY: Int = 1,
    showPoints: Boolean = false,
    indicesModifier: (List<Int>) -> List<Int> = { it }
): Modifier = composed {
    val pointData by remember(points, resolutionX, resolutionY) {
        derivedStateOf {
            PointData(
                points = points,
                stepsX = resolutionX,
                stepsY = resolutionY
            )
        }
    }

    val pointSize = with(LocalDensity.current) { 3.dp.toPx() }

    val pointsPaint = remember {
        Paint().apply {
            color = Color.White.copy(alpha = .9f)
            strokeWidth = pointSize
            strokeCap = StrokeCap.Round
            blendMode = BlendMode.SrcOver
        }
    }
    val meshGraphicLayer = rememberGraphicsLayer()
    val blurRadius = with(LocalDensity.current) { blurLevel.toDp().toPx() }
    val paint = remember { Paint() }

    return@composed drawWithCache {
        onDrawWithContent {
            // Record content on a visible graphics layer
            meshGraphicLayer.apply {
                // Only non-zero blur radii are valid BlurEffect parameters
                this.renderEffect =
                    if (blurRadius > 0f && blurRadius > 0f) {
                        BlurEffect(blurRadius, blurRadius, TileMode.Clamp)
                    } else {
                        null
                    }

                this.clip = false
            }

            meshGraphicLayer.record {
                scale(
                    scaleX = size.width,
                    scaleY = size.height,
                    pivot = Offset.Zero
                ) {
                    drawContext.canvas.drawVertices(
                        vertices = Vertices(
                            vertexMode = VertexMode.Triangles,
                            positions = pointData.offsets,
                            textureCoordinates = pointData.offsets,
                            colors = pointData.colors,
                            indices = indicesModifier(pointData.indices)
                        ),
                        blendMode = gradientBlendMode,
                        paint = paint
                    )
                }
            }

            drawLayer(meshGraphicLayer)

            if (showPoints) {
                drawIntoCanvas { canvas ->
                    val intermediatePoints = pointData.offsets
                        .map { offset -> Offset(offset.x * size.width, offset.y * size.height) }

                    canvas.drawPoints(
                        pointMode = PointMode.Points,
                        points = intermediatePoints,
                        paint = pointsPaint
                    )
                }
            }
        }
    }
}

private class PointData(
    private val points: List<List<Point>>,
    private val stepsX: Int,
    private val stepsY: Int,
) {

    val offsets: MutableList<Offset>
    val colors: MutableList<Color>
    val indices: List<Int>

    private val xLength: Int = (points[0].size * stepsX) - (stepsX - 1)
    private val yLength: Int = (points.size * stepsY) - (stepsY - 1)
    private val measure = PathMeasure()

    private val indicesBlocks: List<IndicesBlock>

    init {
        offsets = buildList {
            repeat((xLength - 0) * (yLength - 0)) {
                add(Offset(0f, 0f))
            }
        }.toMutableList()

        colors = buildList {
            repeat((xLength - 0) * (yLength - 0)) {
                add(Color.Transparent)
            }
        }.toMutableList()

        indicesBlocks = buildList {
            for (y in 0..yLength - 2) {
                for (x in 0..xLength - 2) {

                    val a = (y * xLength) + x
                    val b = a + 1
                    val c = ((y + 1) * xLength) + x
                    val d = c + 1

                    add(
                        IndicesBlock(
                            indices = buildList {
                                add(a)
                                add(c)
                                add(d)

                                add(a)
                                add(b)
                                add(d)
                            },
                            x = x,
                            y = y
                        )
                    )
                }
            }
        }

        indices = indicesBlocks.flatMap { it.indices }

        generateInterpolatedOffsets()
    }

    private fun generateInterpolatedOffsets() {
        for (y in 0..points.lastIndex) {
            for (x in 0..points[y].lastIndex) {
                this[x * stepsX, y * stepsY] = points[y][x].offset
                this[x * stepsX, y * stepsY] = points[y][x].color

                if (x != points[y].lastIndex) {
                    val path = cubicPathX(
                        point1 = points[y][x].offset,
                        point2 = points[y][x + 1].offset,
                        position = when (x) {
                            0 -> 0
                            points[y].lastIndex - 1 -> 2
                            else -> 1
                        }
                    )

                    measure.setPath(path, false)

                    for (i in 1..<stepsX) {
                        measure.getPosition(i / stepsX.toFloat() * measure.length).let {
                            this[(x * stepsX) + i, (y * stepsY)] = Offset(it.x, it.y)
                            this[(x * stepsX) + i, (y * stepsY)] =
                                lerp(
                                    start = points[y][x].color,
                                    stop = points[y][x + 1].color,
                                    fraction = i / stepsX.toFloat(),
                                )
                        }
                    }
                }
            }
        }

        for (y in 0..<points.lastIndex) {
            for (x in 0..<this.xLength) {
                val path = cubicPathY(
                    point1 = this[x, y * stepsY].let { Offset(it.x, it.y) },
                    point2 = this[x, (y + 1) * stepsY].let { Offset(it.x, it.y) },
                    position = when (y) {
                        0 -> 0
                        points[y].lastIndex - 1 -> 2
                        else -> 1
                    }
                )

                measure.setPath(path, false)

                for (i in (1..<stepsY)) {
                    val point3 = measure.getPosition(i / stepsY.toFloat() * measure.length).let {
                        Offset(it.x, it.y)
                    }

                    this[x, ((y * stepsY) + i)] = point3

                    this[x, ((y * stepsY) + i)] = lerp(
                        start = this.getColor(x, y * stepsY),
                        stop = this.getColor(x, (y + 1) * stepsY),
                        fraction = i / stepsY.toFloat(),
                    )
                }
            }
        }
    }

    operator fun get(x: Int, y: Int): Offset {
        val index = (y * xLength) + x
        return offsets[index]
    }

    private fun getColor(x: Int, y: Int): Color {
        val index = (y * xLength) + x
        return colors[index]
    }

    private operator fun set(x: Int, y: Int, offset: Offset) {
        val index = (y * xLength) + x
        offsets[index] = Offset(offset.x, offset.y)
    }

    private operator fun set(x: Int, y: Int, color: Color) {
        val index = (y * xLength) + x
        colors[index] = color
    }

    @Immutable
    data class IndicesBlock(
        val indices: List<Int>,
        val x: Int,
        val y: Int
    )
}

private fun cubicPathX(point1: Offset, point2: Offset, position: Int): Path {
    val path = Path().apply {
        moveTo(point1.x, point1.y)
        val delta = (point2.x - point1.x) * .5f
        when (position) {
            0 -> cubicTo(
                point1.x, point1.y,
                point2.x - delta, point2.y,
                point2.x, point2.y
            )

            2 -> cubicTo(
                point1.x + delta, point1.y,
                point2.x, point2.y,
                point2.x, point2.y
            )

            else -> cubicTo(
                point1.x + delta, point1.y,
                point2.x - delta, point2.y,
                point2.x, point2.y
            )
        }

        lineTo(point2.x, point2.y)
    }
    return path
}

private fun cubicPathY(point1: Offset, point2: Offset, position: Int): Path {
    val path = Path().apply {
        moveTo(point1.x, point1.y)
        val delta = (point2.y - point1.y) * .5f
        when (position) {
            0 -> cubicTo(
                point1.x, point1.y,
                point2.x, point2.y - delta,
                point2.x, point2.y
            )

            2 -> cubicTo(
                point1.x, point1.y + delta,
                point2.x, point2.y,
                point2.x, point2.y
            )

            else -> cubicTo(
                point1.x, point1.y + delta,
                point2.x, point2.y - delta,
                point2.x, point2.y
            )
        }

        lineTo(point2.x, point2.y)
    }
    return path
}
