package com.example.vicoexample

import android.graphics.Typeface
import android.text.TextUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.chart.line.lineSpec
import com.patrykandpatryk.vico.compose.chart.scroll.ChartScrollSpec
import com.patrykandpatryk.vico.compose.style.ChartStyle
import com.patrykandpatryk.vico.compose.style.LocalChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.axis.Axis
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatryk.vico.core.chart.insets.Insets
import com.patrykandpatryk.vico.core.chart.segment.SegmentProperties
import com.patrykandpatryk.vico.core.component.OverlayingComponent
import com.patrykandpatryk.vico.core.component.marker.MarkerComponent
import com.patrykandpatryk.vico.core.component.shape.LineComponent
import com.patrykandpatryk.vico.core.component.shape.ShapeComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes
import com.patrykandpatryk.vico.core.component.shape.cornered.Corner
import com.patrykandpatryk.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatryk.vico.core.component.text.textComponent
import com.patrykandpatryk.vico.core.context.MeasureContext
import com.patrykandpatryk.vico.core.dimensions.MutableDimensions
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.extension.copyColor
import com.patrykandpatryk.vico.core.marker.Marker
import com.patrykandpatryk.vico.core.marker.MarkerLabelFormatter
import com.patrykandpatryk.vico.core.scroll.AutoScrollCondition
import com.patrykandpatryk.vico.core.scroll.InitialScroll


private val entityColors = longArrayOf(0xFFB983FF)
private val yAxisFormatter =
    AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ -> "%4d".format(value.toInt()) }
@Composable
fun StyledChart(
    xRange: State<Pair<Float, Float>>,
    yRange: State<Pair<Float, Float>>,
    markers: Map<Float, Marker>?,
    producer: ChartEntryModelProducer
) {

    val chart = lineChart(
        lines = entityColors.map { color ->
            lineSpec(
                lineColor = Color.White,
                lineThickness = 1.dp,
                lineCap = StrokeCap.Round,
                lineBackgroundShader = null,
                point = ShapeComponent(
                    Shapes.drawableShape(
                        drawable = AppCompatResources.getDrawable(
                            LocalContext.current,
                            R.drawable.baseline_circle_24
                        )!!, tintDrawable = false, keepAspectRatio = false, null
                    ), color = R.color.white
                ),
                pointSize = 6.dp,
            )
        },

        maxX = xRange.value.second,
        minX = xRange.value.first,
        maxY = yRange.value.second,
        minY = yRange.value.first,
        persistentMarkers = markers
    )

    ProvideChartStyle(
        chartStyle = ChartStyle(
            axis = LocalChartStyle.current.axis.copy(
                axisLabelColor = Color.White,
                axisLineColor = Color.White.copy(alpha = .5f),
                axisGuidelineColor = Color.White.copy(alpha = .5f),
            ),
            lineChart = LocalChartStyle.current.lineChart,
            columnChart = LocalChartStyle.current.columnChart,
            marker = LocalChartStyle.current.marker,
            elevationOverlayColor = Color.Yellow
        )
    )
    {
        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            chart = chart,
            chartModelProducer = producer,
            startAxis = startAxis(
                maxLabelCount = 8,
                valueFormatter = yAxisFormatter,
                sizeConstraint = Axis.SizeConstraint.Exact(64f)
            ),
            bottomAxis = bottomAxis(
                sizeConstraint = Axis.SizeConstraint.Exact(32f),
                tickPosition = HorizontalAxis.TickPosition.Center(offset = 0, spacing = 10)
            ),
            diffAnimationSpec = snap(),
            chartScrollSpec = ChartScrollSpec(
                isScrollEnabled = false,
                initialScroll = InitialScroll.Start,
                autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased,
                autoScrollAnimationSpec = TweenSpec(10, 10, FastOutSlowInEasing)
            ),
            isZoomEnabled = false
        )
    }
}

private const val SHADOW_RADIUS = 4f
private const val SHADOW_RADIUS_TO_PX_MULTIPLIER = 1.3f
private const val SHADOW_DY = 2f
private const val GUIDELINE_ALPHA = 0.2f
private const val INDICATOR_SIZE_DP = 36f


fun getMarker(labelString: String): Marker {

    val labelBackgroundShape = MarkerCorneredShape(all = Corner.FullyRounded)
    val label = textComponent {
        color = 0xFFFFFFFF.toInt()
        ellipsize = TextUtils.TruncateAt.END
        lineCount = 1
        padding = MutableDimensions(horizontalDp = 8f, verticalDp = 4f)
        typeface = Typeface.MONOSPACE
        background = ShapeComponent(shape = labelBackgroundShape, color = 0xFFB983FF.toInt())
            .setShadow(radius = SHADOW_RADIUS, dy = SHADOW_DY, applyElevationOverlay = true)
    }

    val indicatorInner = ShapeComponent(shape = Shapes.pillShape, color = R.color.white, strokeColor = R.color.white)
    val indicatorCenter = ShapeComponent(shape = Shapes.pillShape, color = R.color.white, strokeColor = R.color.white)
    val indicatorOuter = ShapeComponent(shape = Shapes.pillShape, color = R.color.white, strokeColor = R.color.white)

    val indicator = OverlayingComponent(
        outer = indicatorOuter,
        innerPaddingAllDp = 10f,
        inner = OverlayingComponent(
            outer = indicatorCenter,
            inner = indicatorInner,
            innerPaddingAllDp = 5f,
        ),
    )

    val guideline = LineComponent(
        color = 0xFFB983FF.toInt(),
        thicknessDp = 2f,
        strokeColor = 0xFFB983FF.toInt()

    )

    return object : MarkerComponent(
        label = label,
        indicator = indicator,
        guideline = guideline,
    ) {
        init {
            indicatorSizeDp = INDICATOR_SIZE_DP
            onApplyEntryColor = { entryColor ->
                indicatorOuter.color = entryColor.copyColor(alpha = 32)
                with(indicatorCenter) {
                    color = entryColor
                    setShadow(radius = 12f, color = entryColor)
                }
            }
            labelFormatter = MarkerLabelFormatter { labelString }
        }

        override fun getInsets(
            context: MeasureContext,
            outInsets: Insets,
            segmentProperties: SegmentProperties,
        ) = with(context) {
            outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                    SHADOW_RADIUS.pixels * SHADOW_RADIUS_TO_PX_MULTIPLIER - SHADOW_DY.pixels
        }
    }
}
