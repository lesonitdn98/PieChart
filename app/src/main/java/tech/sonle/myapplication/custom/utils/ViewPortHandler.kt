package tech.sonle.myapplication.custom.utils

import android.graphics.Matrix
import android.graphics.RectF
import android.view.View

/**
 * Class that contains information about the charts current viewport settings, including offsets, scale & translation
 * levels, ...
 *
 * Create by SonLe on 19/04/2022
 */
class ViewPortHandler {
    /**
     * matrix used for touch events
     */
    protected val mMatrixTouch = Matrix()

    /**
     * this rectangle defines the area in which graph values can be drawn
     */
    protected var mContentRect = RectF()

    protected var mChartWidth = 0f
    protected var mChartHeight = 0f

    /**
     * minimum scale value on the y-axis
     */
    private var mMinScaleY = 1f

    /**
     * maximum scale value on the y-axis
     */
    private var mMaxScaleY = Float.MAX_VALUE

    /**
     * minimum scale value on the x-axis
     */
    private var mMinScaleX = 1f

    /**
     * maximum scale value on the x-axis
     */
    private var mMaxScaleX = Float.MAX_VALUE

    /**
     * contains the current scale factor of the x-axis
     */
    private var mScaleX = 1f

    /**
     * contains the current scale factor of the y-axis
     */
    private var mScaleY = 1f

    /**
     * current translation (drag distance) on the x-axis
     */
    private var mTransX = 0f

    /**
     * current translation (drag distance) on the y-axis
     */
    private var mTransY = 0f

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private var mTransOffsetX = 0f

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private var mTransOffsetY = 0f

    /**
     * Constructor - don't forget calling setChartDimens(...)
     */
    fun ViewPortHandler() {}

    /**
     * Sets the width and height of the chart.
     *
     * @param width
     * @param height
     */
    fun setChartDimens(width: Float, height: Float) {
        val offsetLeft = offsetLeft()
        val offsetTop = offsetTop()
        val offsetRight = offsetRight()
        val offsetBottom = offsetBottom()
        mChartHeight = height
        mChartWidth = width
        restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom)
    }

    fun hasChartDimens(): Boolean {
        return mChartHeight > 0 && mChartWidth > 0
    }

    fun restrainViewPort(
        offsetLeft: Float, offsetTop: Float, offsetRight: Float,
        offsetBottom: Float
    ) {
        mContentRect[offsetLeft, offsetTop, mChartWidth - offsetRight] = (mChartHeight
                - offsetBottom)
    }

    fun offsetLeft(): Float {
        return mContentRect.left
    }

    fun offsetRight(): Float {
        return mChartWidth - mContentRect.right
    }

    fun offsetTop(): Float {
        return mContentRect.top
    }

    fun offsetBottom(): Float {
        return mChartHeight - mContentRect.bottom
    }

    fun contentTop(): Float {
        return mContentRect.top
    }

    fun contentLeft(): Float {
        return mContentRect.left
    }

    fun contentRight(): Float {
        return mContentRect.right
    }

    fun contentBottom(): Float {
        return mContentRect.bottom
    }

    fun contentWidth(): Float {
        return mContentRect.width()
    }

    fun contentHeight(): Float {
        return mContentRect.height()
    }

    fun getContentRect(): RectF {
        return mContentRect
    }

    fun getContentCenter(): MPPointF? {
        return MPPointF.getInstance(mContentRect.centerX(), mContentRect.centerY())
    }

    fun getChartHeight(): Float {
        return mChartHeight
    }

    fun getChartWidth(): Float {
        return mChartWidth
    }

    /**
     * Returns the smallest extension of the content rect (width or height).
     *
     * @return
     */
    fun getSmallestContentExtension(): Float {
        return Math.min(mContentRect.width(), mContentRect.height())
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO SCALING AND GESTURES */

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO SCALING AND GESTURES  */
    /**
     * Zooms in by 1.4f, x and y are the coordinates (in pixels) of the zoom
     * center.
     *
     * @param x
     * @param y
     */
    fun zoomIn(x: Float, y: Float): Matrix? {
        val save = Matrix()
        zoomIn(x, y, save)
        return save
    }

    fun zoomIn(x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.postScale(1.4f, 1.4f, x, y)
    }

    /**
     * Zooms out by 0.7f, x and y are the coordinates (in pixels) of the zoom
     * center.
     */
    fun zoomOut(x: Float, y: Float): Matrix? {
        val save = Matrix()
        zoomOut(x, y, save)
        return save
    }

    fun zoomOut(x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.postScale(0.7f, 0.7f, x, y)
    }

    /**
     * Zooms out to original size.
     * @param outputMatrix
     */
    fun resetZoom(outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.postScale(1.0f, 1.0f, 0.0f, 0.0f)
    }

    /**
     * Post-scales by the specified scale factors.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    fun zoom(scaleX: Float, scaleY: Float): Matrix? {
        val save = Matrix()
        zoom(scaleX, scaleY, save)
        return save
    }

    fun zoom(scaleX: Float, scaleY: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.postScale(scaleX, scaleY)
    }

    /**
     * Post-scales by the specified scale factors. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    fun zoom(scaleX: Float, scaleY: Float, x: Float, y: Float): Matrix? {
        val save = Matrix()
        zoom(scaleX, scaleY, x, y, save)
        return save
    }

    fun zoom(scaleX: Float, scaleY: Float, x: Float, y: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.postScale(scaleX, scaleY, x, y)
    }

    /**
     * Sets the scale factor to the specified values.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    fun setZoom(scaleX: Float, scaleY: Float): Matrix? {
        val save = Matrix()
        setZoom(scaleX, scaleY, save)
        return save
    }

    fun setZoom(scaleX: Float, scaleY: Float, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        outputMatrix.setScale(scaleX, scaleY)
    }

    /**
     * Sets the scale factor to the specified values. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    fun setZoom(scaleX: Float, scaleY: Float, x: Float, y: Float): Matrix? {
        val save = Matrix()
        save.set(mMatrixTouch)
        save.setScale(scaleX, scaleY, x, y)
        return save
    }

    protected var valsBufferForFitScreen = FloatArray(9)

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    fun fitScreen(): Matrix? {
        val save = Matrix()
        fitScreen(save)
        return save
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.  Output Matrix is available for those who wish to cache the object.
     */
    fun fitScreen(outputMatrix: Matrix) {
        mMinScaleX = 1f
        mMinScaleY = 1f
        outputMatrix.set(mMatrixTouch)
        val vals = valsBufferForFitScreen
        for (i in 0..8) {
            vals[i] = 0F
        }
        outputMatrix.getValues(vals)

        // reset all translations and scaling
        vals[Matrix.MTRANS_X] = 0f
        vals[Matrix.MTRANS_Y] = 0f
        vals[Matrix.MSCALE_X] = 1f
        vals[Matrix.MSCALE_Y] = 1f
        outputMatrix.setValues(vals)
    }

    /**
     * Post-translates to the specified points.  Less Performant.
     *
     * @param transformedPts
     * @return
     */
    fun translate(transformedPts: FloatArray): Matrix? {
        val save = Matrix()
        translate(transformedPts, save)
        return save
    }

    /**
     * Post-translates to the specified points.  Output matrix allows for caching objects.
     *
     * @param transformedPts
     * @return
     */
    fun translate(transformedPts: FloatArray, outputMatrix: Matrix) {
        outputMatrix.reset()
        outputMatrix.set(mMatrixTouch)
        val x = transformedPts[0] - offsetLeft()
        val y = transformedPts[1] - offsetTop()
        outputMatrix.postTranslate(-x, -y)
    }

    protected var mCenterViewPortMatrixBuffer = Matrix()

    /**
     * Centers the viewport around the specified position (x-index and y-value)
     * in the chart. Centering the viewport outside the bounds of the chart is
     * not possible. Makes most sense in combination with the
     * setScaleMinima(...) method.
     *
     * @param transformedPts the position to center view viewport to
     * @param view
     * @return save
     */
    fun centerViewPort(transformedPts: FloatArray, view: View) {
        val save = mCenterViewPortMatrixBuffer
        save.reset()
        save.set(mMatrixTouch)
        val x = transformedPts[0] - offsetLeft()
        val y = transformedPts[1] - offsetTop()
        save.postTranslate(-x, -y)
        refresh(save, view, true)
    }

    /**
     * buffer for storing the 9 matrix values of a 3x3 matrix
     */
    protected val matrixBuffer = FloatArray(9)

    /**
     * call this method to refresh the graph with a given matrix
     *
     * @param newMatrix
     * @return
     */
    fun refresh(newMatrix: Matrix, chart: View, invalidate: Boolean): Matrix? {
        mMatrixTouch.set(newMatrix)

        // make sure scale and translation are within their bounds
        limitTransAndScale(mMatrixTouch, mContentRect)
        if (invalidate) chart.invalidate()
        newMatrix.set(mMatrixTouch)
        return newMatrix
    }

    /**
     * limits the maximum scale and X translation of the given matrix
     *
     * @param matrix
     */
    fun limitTransAndScale(matrix: Matrix, content: RectF?) {
        matrix.getValues(matrixBuffer)
        val curTransX = matrixBuffer[Matrix.MTRANS_X]
        val curScaleX = matrixBuffer[Matrix.MSCALE_X]
        val curTransY = matrixBuffer[Matrix.MTRANS_Y]
        val curScaleY = matrixBuffer[Matrix.MSCALE_Y]

        // min scale-x is 1f
        mScaleX = Math.min(Math.max(mMinScaleX, curScaleX), mMaxScaleX)

        // min scale-y is 1f
        mScaleY = Math.min(Math.max(mMinScaleY, curScaleY), mMaxScaleY)
        var width = 0f
        var height = 0f
        if (content != null) {
            width = content.width()
            height = content.height()
        }
        val maxTransX = -width * (mScaleX - 1f)
        mTransX = Math.min(Math.max(curTransX, maxTransX - mTransOffsetX), mTransOffsetX)
        val maxTransY = height * (mScaleY - 1f)
        mTransY = Math.max(Math.min(curTransY, maxTransY + mTransOffsetY), -mTransOffsetY)
        matrixBuffer[Matrix.MTRANS_X] = mTransX
        matrixBuffer[Matrix.MSCALE_X] = mScaleX
        matrixBuffer[Matrix.MTRANS_Y] = mTransY
        matrixBuffer[Matrix.MSCALE_Y] = mScaleY
        matrix.setValues(matrixBuffer)
    }

    /**
     * Sets the minimum scale factor for the x-axis
     *
     * @param xScale
     */
    fun setMinimumScaleX(xScale: Float) {
        var xScale = xScale
        if (xScale < 1f) xScale = 1f
        mMinScaleX = xScale
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    /**
     * Sets the maximum scale factor for the x-axis
     *
     * @param xScale
     */
    fun setMaximumScaleX(xScale: Float) {
        var xScale = xScale
        if (xScale == 0f) xScale = Float.MAX_VALUE
        mMaxScaleX = xScale
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    /**
     * Sets the minimum and maximum scale factors for the x-axis
     *
     * @param minScaleX
     * @param maxScaleX
     */
    fun setMinMaxScaleX(minScaleX: Float, maxScaleX: Float) {
        var minScaleX = minScaleX
        var maxScaleX = maxScaleX
        if (minScaleX < 1f) minScaleX = 1f
        if (maxScaleX == 0f) maxScaleX = Float.MAX_VALUE
        mMinScaleX = minScaleX
        mMaxScaleX = maxScaleX
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    /**
     * Sets the minimum scale factor for the y-axis
     *
     * @param yScale
     */
    fun setMinimumScaleY(yScale: Float) {
        var yScale = yScale
        if (yScale < 1f) yScale = 1f
        mMinScaleY = yScale
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    /**
     * Sets the maximum scale factor for the y-axis
     *
     * @param yScale
     */
    fun setMaximumScaleY(yScale: Float) {
        var yScale = yScale
        if (yScale == 0f) yScale = Float.MAX_VALUE
        mMaxScaleY = yScale
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    fun setMinMaxScaleY(minScaleY: Float, maxScaleY: Float) {
        var minScaleY = minScaleY
        var maxScaleY = maxScaleY
        if (minScaleY < 1f) minScaleY = 1f
        if (maxScaleY == 0f) maxScaleY = Float.MAX_VALUE
        mMinScaleY = minScaleY
        mMaxScaleY = maxScaleY
        limitTransAndScale(mMatrixTouch, mContentRect)
    }

    /**
     * Returns the charts-touch matrix used for translation and scale on touch.
     *
     * @return
     */
    fun getMatrixTouch(): Matrix? {
        return mMatrixTouch
    }

    /**
     * ################ ################ ################ ################
     */
    /**
     * ################ ################ ################ ################
     */
    /**
     * BELOW METHODS FOR BOUNDS CHECK
     */
    fun isInBoundsX(x: Float): Boolean {
        return isInBoundsLeft(x) && isInBoundsRight(x)
    }

    fun isInBoundsY(y: Float): Boolean {
        return isInBoundsTop(y) && isInBoundsBottom(y)
    }

    fun isInBounds(x: Float, y: Float): Boolean {
        return isInBoundsX(x) && isInBoundsY(y)
    }

    fun isInBoundsLeft(x: Float): Boolean {
        return mContentRect.left <= x + 1
    }

    fun isInBoundsRight(x: Float): Boolean {
        var x = x
        x = (x * 100f).toInt().toFloat() / 100f
        return mContentRect.right >= x - 1
    }

    fun isInBoundsTop(y: Float): Boolean {
        return mContentRect.top <= y
    }

    fun isInBoundsBottom(y: Float): Boolean {
        var y = y
        y = (y * 100f).toInt().toFloat() / 100f
        return mContentRect.bottom >= y
    }

    /**
     * returns the current x-scale factor
     */
    fun getScaleX(): Float {
        return mScaleX
    }

    /**
     * returns the current y-scale factor
     */
    fun getScaleY(): Float {
        return mScaleY
    }

    fun getMinScaleX(): Float {
        return mMinScaleX
    }

    fun getMaxScaleX(): Float {
        return mMaxScaleX
    }

    fun getMinScaleY(): Float {
        return mMinScaleY
    }

    fun getMaxScaleY(): Float {
        return mMaxScaleY
    }

    /**
     * Returns the translation (drag / pan) distance on the x-axis
     *
     * @return
     */
    fun getTransX(): Float {
        return mTransX
    }

    /**
     * Returns the translation (drag / pan) distance on the y-axis
     *
     * @return
     */
    fun getTransY(): Float {
        return mTransY
    }

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    fun isFullyZoomedOut(): Boolean {
        return isFullyZoomedOutX() && isFullyZoomedOutY()
    }

    /**
     * Returns true if the chart is fully zoomed out on it's y-axis (vertical).
     *
     * @return
     */
    fun isFullyZoomedOutY(): Boolean {
        return !(mScaleY > mMinScaleY || mMinScaleY > 1f)
    }

    /**
     * Returns true if the chart is fully zoomed out on it's x-axis
     * (horizontal).
     *
     * @return
     */
    fun isFullyZoomedOutX(): Boolean {
        return !(mScaleX > mMinScaleX || mMinScaleX > 1f)
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     *
     * @param offset
     */
    fun setDragOffsetX(offset: Float) {
        mTransOffsetX = Utils.convertDpToPixel(offset)
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     *
     * @param offset
     */
    fun setDragOffsetY(offset: Float) {
        mTransOffsetY = Utils.convertDpToPixel(offset)
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    fun hasNoDragOffset(): Boolean {
        return mTransOffsetX <= 0 && mTransOffsetY <= 0
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the x-axis
     *
     * @return
     */
    fun canZoomOutMoreX(): Boolean {
        return mScaleX > mMinScaleX
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the x-axis
     *
     * @return
     */
    fun canZoomInMoreX(): Boolean {
        return mScaleX < mMaxScaleX
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the y-axis
     *
     * @return
     */
    fun canZoomOutMoreY(): Boolean {
        return mScaleY > mMinScaleY
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the y-axis
     *
     * @return
     */
    fun canZoomInMoreY(): Boolean {
        return mScaleY < mMaxScaleY
    }
}