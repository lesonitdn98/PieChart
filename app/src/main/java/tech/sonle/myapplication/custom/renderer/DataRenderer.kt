package tech.sonle.myapplication.custom.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Typeface
import tech.sonle.myapplication.custom.animation.ChartAnimator
import tech.sonle.myapplication.custom.data.Entry
import tech.sonle.myapplication.custom.formatter.IValueFormatter
import tech.sonle.myapplication.custom.highlight.Highlight
import tech.sonle.myapplication.custom.interfaces.dataprovider.ChartInterface
import tech.sonle.myapplication.custom.interfaces.datasets.IDataSet
import tech.sonle.myapplication.custom.utils.Utils
import tech.sonle.myapplication.custom.utils.ViewPortHandler


/**
 * Superclass of all render classes for the different data types (line, bar, ...).
 *
 * Create by SonLe on 19/04/2022
 */
abstract class DataRenderer : Renderer() {
    /**
     * the animator object used to perform animations on the chart data
     */
    protected var mAnimator: ChartAnimator? = null

    /**
     * main paint object used for rendering
     */
    protected var mRenderPaint: Paint? = null

    /**
     * paint used for highlighting values
     */
    protected var mHighlightPaint: Paint? = null

    protected var mDrawPaint: Paint? = null

    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    protected var mValuePaint: Paint? = null

    open fun DataRenderer(animator: ChartAnimator?, viewPortHandler: ViewPortHandler?) {
        super.Renderer(viewPortHandler)
        mAnimator = animator
        mRenderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRenderPaint!!.style = Paint.Style.FILL
        mDrawPaint = Paint(Paint.DITHER_FLAG)
        mValuePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mValuePaint!!.color = Color.rgb(63, 63, 63)
        mValuePaint!!.textAlign = Align.CENTER
        mValuePaint!!.textSize = Utils.convertDpToPixel(9f)
        mHighlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHighlightPaint!!.style = Paint.Style.STROKE
        mHighlightPaint!!.strokeWidth = 2f
        mHighlightPaint!!.color = Color.rgb(255, 187, 115)
    }

    protected open fun isDrawingValuesAllowed(chart: ChartInterface): Boolean {
        return chart.data.entryCount < chart.maxVisibleCount * mViewPortHandler!!.getScaleX()
    }

    /**
     * Returns the Paint object this renderer uses for drawing the values
     * (value-text).
     *
     * @return
     */
    open fun getPaintValues(): Paint? {
        return mValuePaint
    }

    /**
     * Returns the Paint object this renderer uses for drawing highlight
     * indicators.
     *
     * @return
     */
    open fun getPaintHighlight(): Paint? {
        return mHighlightPaint
    }

    /**
     * Returns the Paint object used for rendering.
     *
     * @return
     */
    open fun getPaintRender(): Paint? {
        return mRenderPaint
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     * @param set
     */
    protected open fun applyValueTextStyle(set: IDataSet<Entry>) {
        mValuePaint!!.typeface = set.valueTypeface
        mValuePaint!!.textSize = set.valueTextSize
    }

    /**
     * Initializes the buffers used for rendering with a new size. Since this
     * method performs memory allocations, it should only be called if
     * necessary.
     */
    abstract fun initBuffers()

    /**
     * Draws the actual data in form of lines, bars, ... depending on Renderer subclass.
     *
     * @param c
     */
    abstract fun drawData(c: Canvas?)

    /**
     * Loops over all Entrys and draws their values.
     *
     * @param c
     */
    abstract fun drawValues(c: Canvas?)

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c            canvas
     * @param formatter    formatter for custom value-formatting
     * @param value        the value to be drawn
     * @param entry        the entry the value belongs to
     * @param dataSetIndex the index of the DataSet the drawn Entry belongs to
     * @param x            position
     * @param y            position
     * @param color
     */
    open fun drawValue(
        c: Canvas,
        formatter: IValueFormatter,
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        x: Float,
        y: Float,
        color: Int
    ) {
        val strokePaint = Paint()
        strokePaint.setARGB(255, 0, 0, 0)
        strokePaint.textAlign = Align.CENTER
        strokePaint.textSize = 16f
        strokePaint.typeface = Typeface.DEFAULT_BOLD
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2f

        mValuePaint!!.color = color

        c.drawText(
            formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y,
            strokePaint
        )
        c.drawText(
            formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler), x, y,
            mValuePaint!!
        )
    }

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c            canvas
     * @param label
     * @param formatter    formatter for custom value-formatting
     * @param value        the value to be drawn
     * @param entry        the entry the value belongs to
     * @param dataSetIndex the index of the DataSet the drawn Entry belongs to
     * @param x            position
     * @param y            position
     * @param color
     */
    open fun drawValueWithLabel(
        c: Canvas,
        label: String,
        formatter: IValueFormatter,
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        x: Float,
        y: Float,
        color: Int
    ) {
        mValuePaint!!.color = color
        c.drawText(
            "$label: ${formatter.getFormattedValue(value, entry, dataSetIndex, mViewPortHandler)}",
            x,
            y,
            mValuePaint!!
        )
    }

    /**
     * Draws any kind of additional information (e.g. line-circles).
     *
     * @param c
     */
    abstract fun drawExtras(c: Canvas?)

    /**
     * Draws all highlight indicators for the values that are currently highlighted.
     *
     * @param c
     * @param indices the highlighted values
     */
    abstract fun drawHighlighted(c: Canvas?, indices: Array<Highlight?>?)
}