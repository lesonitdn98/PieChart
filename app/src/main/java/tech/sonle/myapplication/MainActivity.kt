package tech.sonle.myapplication

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import tech.sonle.myapplication.custom.PieChart
import tech.sonle.myapplication.custom.animation.Easing
import tech.sonle.myapplication.custom.data.Entry
import tech.sonle.myapplication.custom.data.PieData
import tech.sonle.myapplication.custom.data.PieDataSet
import tech.sonle.myapplication.custom.data.PieEntry
import tech.sonle.myapplication.custom.highlight.Highlight
import tech.sonle.myapplication.custom.listener.OnChartValueSelectedListener
import tech.sonle.myapplication.custom.utils.ColorTemplate
import tech.sonle.myapplication.custom.utils.MPPointF

class MainActivity : AppCompatActivity(), OnChartValueSelectedListener {

    var parties = arrayOf(
        "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
        "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
        "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
        "Party Y", "Party Z"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chart = findViewById<PieChart>(R.id.chart1)

        chart.setUsePercentValues(true)
        chart.setExtraOffsets(5F, 10F, 5F, 5F)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 40f
        chart.transparentCircleRadius = 42f

        chart.rotationAngle = 0F
        // enable rotation of the chart by touch
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        chart.labelShadowColor = ContextCompat.getColor(this, R.color.dove_gray)

        chart.setOnChartValueSelectedListener(this)

        chart.animateY(1400, Easing.EaseInOutQuad)
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(12f)

        setData(chart, 5, 5F)
    }

    private fun setData(chart: PieChart, count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(R.drawable.ic_launcher_background)
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        //dataSet.setSelectionShift(0f);
        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor(this, R.color.tory_blue))
//        data.setValueTypeface(tfLight)
        chart.data = data

        // undo all highlights
        chart.highlightValues(ArrayList())
        chart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        TODO("Not yet implemented")
        println("sd")
    }

    override fun onNothingSelected() {
//        TODO("Not yet implemented")
    }
}