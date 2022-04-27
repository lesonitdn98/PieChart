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

    var parties = arrayOf("Cardboard", "Paper", "Plastic", "Metal", "Aluminium", "Beverage carton", "Polystyrene", "Fabric", "Light bulb", "Glass")

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

        setData(chart, 2, 5F)
    }

    private fun setData(chart: PieChart, count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()

        entries.add(
            PieEntry(
                1F,
                "Test",
                ContextCompat.getColor(this, R.color.dove_gray)
            )
        )

        entries.add(
            PieEntry(
                2F,
                "Test",
                ContextCompat.getColor(this, R.color.tory_blue)
            )
        )

        val dataSet = PieDataSet(entries, "Election Results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

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