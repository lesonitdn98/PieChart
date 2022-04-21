
package tech.sonle.myapplication.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.List;

import tech.sonle.myapplication.custom.components.XAxis;
import tech.sonle.myapplication.custom.data.PieData;
import tech.sonle.myapplication.custom.highlight.Highlight;
import tech.sonle.myapplication.custom.highlight.PieHighlighter;
import tech.sonle.myapplication.custom.interfaces.datasets.IPieDataSet;
import tech.sonle.myapplication.custom.renderer.PieChartRenderer;
import tech.sonle.myapplication.custom.utils.MPPointF;
import tech.sonle.myapplication.custom.utils.Utils;

/**
 * View that represents a pie chart. Draws cake like slices.
 */
public class PieChart extends PieRadarChartBase<PieData> {

    /**
     * rect object that represents the bounds of the piechart, needed for
     * drawing the circle
     */
    private RectF mCircleBox = new RectF();

    /**
     * flag indicating if entry labels should be drawn or not
     */
    private boolean mDrawEntryLabels = true;

    /**
     * array that holds the width of each pie-slice in degrees
     */
    private float[] mDrawAngles = new float[1];

    /**
     * array that holds the absolute angle in degrees of each slice
     */
    private float[] mAbsoluteAngles = new float[1];

    /**
     * if true, the white hole inside the chart will be drawn
     */
    private boolean mDrawHole = true;

    /**
     * if true, the hole will see-through to the inner tips of the slices
     */
    private boolean mDrawSlicesUnderHole = false;

    /**
     * if true, the values inside the piechart are drawn as percent values
     */
    private boolean mUsePercentValues = false;

    /**
     * if true, the slices of the piechart are rounded
     */
    private boolean mDrawRoundedSlices = false;

    /**
     * indicates the size of the hole in the center of the piechart, default:
     * radius / 2
     */
    private float mHoleRadiusPercent = 50f;

    /**
     * the radius of the transparent circle next to the chart-hole in the center
     */
    protected float mTransparentCircleRadiusPercent = 55f;

    protected float mMaxAngle = 360f;

    /**
     * Minimum angle to draw slices, this only works if there is enough room for all slices to have
     * the minimum angle, default 0f.
     */
    private float mMinAngleForSlices = 0f;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new PieChartRenderer(this, mAnimator, mViewPortHandler);
        mXAxis = null;

        mHighlighter = new PieHighlighter(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

        mRenderer.drawData(canvas);

        if (valuesToHighlight())
            mRenderer.drawHighlighted(canvas, mIndicesToHighlight);

        mRenderer.drawExtras(canvas);

        mRenderer.drawValues(canvas);
    }

    @Override
    public void calculateOffsets() {
        super.calculateOffsets();

        // prevent nullpointer when no data set
        if (mData == null)
            return;

        float diameter = getDiameter();
        float radius = diameter / 3f;

        MPPointF c = getCenterOffsets();

        float shift = mData.getDataSet().getSelectionShift();

        // create the circle box that will contain the pie-chart (the bounds of
        // the pie-chart)
        mCircleBox.set(c.x - radius + shift,
                c.y - radius + shift,
                c.x + radius - shift,
                c.y + radius - shift);

        MPPointF.recycleInstance(c);
    }

    @Override
    protected void calcMinMax() {
        calcAngles();
    }

    /**
     * calculates the needed angles for the chart slices
     */
    private void calcAngles() {

        int entryCount = mData.getEntryCount();

        if (mDrawAngles.length != entryCount) {
            mDrawAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mDrawAngles[i] = 0;
            }
        }
        if (mAbsoluteAngles.length != entryCount) {
            mAbsoluteAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                mAbsoluteAngles[i] = 0;
            }
        }

        float yValueSum = mData.getYValueSum();

        List<IPieDataSet> dataSets = mData.getDataSets();

        boolean hasMinAngle = mMinAngleForSlices != 0f && entryCount * mMinAngleForSlices <= mMaxAngle;
        float[] minAngles = new float[entryCount];

        int cnt = 0;
        float offset = 0f;
        float diff = 0f;

        for (int i = 0; i < mData.getDataSetCount(); i++) {

            IPieDataSet set = dataSets.get(i);

            for (int j = 0; j < set.getEntryCount(); j++) {

                float drawAngle = calcAngle(Math.abs(set.getEntryForIndex(j).getY()), yValueSum);

                if (hasMinAngle) {
                    float temp = drawAngle - mMinAngleForSlices;
                    if (temp <= 0) {
                        minAngles[cnt] = mMinAngleForSlices;
                        offset += -temp;
                    } else {
                        minAngles[cnt] = drawAngle;
                        diff += temp;
                    }
                }

                mDrawAngles[cnt] = drawAngle;

                if (cnt == 0) {
                    mAbsoluteAngles[cnt] = mDrawAngles[cnt];
                } else {
                    mAbsoluteAngles[cnt] = mAbsoluteAngles[cnt - 1] + mDrawAngles[cnt];
                }

                cnt++;
            }
        }

        if (hasMinAngle) {
            // Correct bigger slices by relatively reducing their angles based on the total angle needed to subtract
            // This requires that `entryCount * mMinAngleForSlices <= mMaxAngle` be true to properly work!
            for (int i = 0; i < entryCount; i++) {
                minAngles[i] -= (minAngles[i] - mMinAngleForSlices) / diff * offset;
                if (i == 0) {
                    mAbsoluteAngles[0] = minAngles[0];
                } else {
                    mAbsoluteAngles[i] = mAbsoluteAngles[i - 1] + minAngles[i];
                }
            }

            mDrawAngles = minAngles;
        }
    }

    /**
     * Checks if the given index is set to be highlighted.
     */
    public boolean needsHighlight(int index) {
        // no highlight
        if (!valuesToHighlight())
            return false;

        // check if the xvalue for the given dataset needs highlight
        for (Highlight highlight : mIndicesToHighlight)
            if ((int) highlight.getX() == index)
                return true;

        return false;
    }

    /**
     * calculates the needed angle for a given value
     */
    private float calcAngle(float value) {
        return calcAngle(value, mData.getYValueSum());
    }

    /**
     * calculates the needed angle for a given value
     */
    private float calcAngle(float value, float yValueSum) {
        return value / yValueSum * mMaxAngle;
    }

    /**
     * This will throw an exception, PieChart has no XAxis object.
     */
    @Deprecated
    @Override
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }

    @Override
    public int getIndexForAngle(float angle) {

        // take the current angle of the chart into consideration
        float a = Utils.Companion.getNormalizedAngle(angle - getRotationAngle());

        for (int i = 0; i < mAbsoluteAngles.length; i++) {
            if (mAbsoluteAngles[i] > a)
                return i;
        }

        return -1; // return -1 if no index found
    }

    /**
     * Returns the index of the DataSet this x-index belongs to.
     */
    public int getDataSetIndexForIndex(int xIndex) {

        List<IPieDataSet> dataSets = mData.getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {
            if (dataSets.get(i).getEntryForXValue(xIndex, Float.NaN) != null)
                return i;
        }

        return -1;
    }

    /**
     * returns an integer array of all the different angles the chart slices
     * have the angles in the returned array determine how much space (of 360°)
     * each slice takes
     */
    public float[] getDrawAngles() {
        return mDrawAngles;
    }

    /**
     * returns the absolute angles of the different chart slices (where the
     * slices end)
     */
    public float[] getAbsoluteAngles() {
        return mAbsoluteAngles;
    }

    /**
     * Sets the color for the hole that is drawn in the center of the PieChart
     * (if enabled).
     */
    public void setHoleColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintHole().setColor(color);
    }

    /**
     * Enable or disable the visibility of the inner tips of the slices behind the hole
     */
    public void setDrawSlicesUnderHole(boolean enable) {
        mDrawSlicesUnderHole = enable;
    }

    /**
     * Returns true if the inner tips of the slices are visible behind the hole,
     * false if not.
     *
     * @return true if slices are visible behind the hole.
     */
    public boolean isDrawSlicesUnderHoleEnabled() {
        return mDrawSlicesUnderHole;
    }

    /**
     * set this to true to draw the pie center empty
     */
    public void setDrawHoleEnabled(boolean enabled) {
        this.mDrawHole = enabled;
    }

    /**
     * returns true if the hole in the center of the pie-chart is set to be
     * visible, false if not
     */
    public boolean isDrawHoleEnabled() {
        return mDrawHole;
    }

    @Override
    protected float getRequiredBaseOffset() {
        return 0;
    }

    @Override
    public float getRadius() {
        if (mCircleBox == null)
            return 0;
        else
            return Math.min(mCircleBox.width() / 2f, mCircleBox.height() / 2f);
    }

    @Override
    protected float getRequiredLegendOffset() {
        return 0;
    }

    /**
     * returns the circlebox, the boundingbox of the pie-chart slices
     */
    public RectF getCircleBox() {
        return mCircleBox;
    }

    /**
     * returns the center of the circlebox
     */
    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(mCircleBox.centerX(), mCircleBox.centerY());
    }

    /**
     * Sets the color of the center text of the PieChart.
     */
    public void setCenterTextColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintCenterText().setColor(color);
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%
     */
    public void setHoleRadius(final float percent) {
        mHoleRadiusPercent = percent;
    }

    /**
     * Returns the size of the hole radius in percent of the total radius.
     */
    public float getHoleRadius() {
        return mHoleRadiusPercent;
    }

    /**
     * Sets the color the transparent-circle should have.
     */
    public void setTransparentCircleColor(int color) {

        Paint p = ((PieChartRenderer) mRenderer).getPaintTransparentCircle();
        int alpha = p.getAlpha();
        p.setColor(color);
        p.setAlpha(alpha);
    }

    /**
     * sets the radius of the transparent circle that is drawn next to the hole
     * in the piechart in percent of the maximum radius (max = the radius of the
     * whole chart), default 55% -> means 5% larger than the center-hole by
     * default
     */
    public void setTransparentCircleRadius(final float percent) {
        mTransparentCircleRadiusPercent = percent;
    }

    public float getTransparentCircleRadius() {
        return mTransparentCircleRadiusPercent;
    }

    /**
     * Sets the amount of transparency the transparent circle should have 0 = fully transparent,
     * 255 = fully opaque.
     * Default value is 100.
     *
     * @param alpha 0-255
     */
    public void setTransparentCircleAlpha(int alpha) {
        ((PieChartRenderer) mRenderer).getPaintTransparentCircle().setAlpha(alpha);
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     * Deprecated -> use setDrawEntryLabels(...) instead.
     */
    @Deprecated
    public void setDrawSliceText(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    /**
     * Set this to true to draw the entry labels into the pie slices (Provided by the getLabel() method of the PieEntry class).
     */
    public void setDrawEntryLabels(boolean enabled) {
        mDrawEntryLabels = enabled;
    }

    /**
     * Returns true if drawing the entry labels is enabled, false if not.
     */
    public boolean isDrawEntryLabelsEnabled() {
        return mDrawEntryLabels;
    }

    /**
     * Sets the color the entry labels are drawn with.
     */
    public void setEntryLabelColor(int color) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setColor(color);
    }

    /**
     * Sets a custom Typeface for the drawing of the entry labels.
     */
    public void setEntryLabelTypeface(Typeface tf) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTypeface(tf);
    }

    /**
     * Sets the size of the entry labels in dp. Default: 13dp
     */
    public void setEntryLabelTextSize(float size) {
        ((PieChartRenderer) mRenderer).getPaintEntryLabels().setTextSize(Utils.Companion.convertDpToPixel(size));
    }

    /**
     * Sets whether to draw slices in a curved fashion, only works if drawing the hole is enabled
     * and if the slices are not drawn under the hole.
     *
     * @param enabled draw curved ends of slices
     */
    public void setDrawRoundedSlices(boolean enabled) {
        mDrawRoundedSlices = enabled;
    }

    /**
     * Returns true if the chart is set to draw each end of a pie-slice
     * "rounded".
     */
    public boolean isDrawRoundedSlicesEnabled() {
        return mDrawRoundedSlices;
    }

    /**
     * If this is enabled, values inside the PieChart are drawn in percent and
     * not with their original value. Values provided for the IValueFormatter to
     * format are then provided in percent.
     */
    public void setUsePercentValues(boolean enabled) {
        mUsePercentValues = enabled;
    }

    /**
     * Returns true if using percentage values is enabled for the chart.
     */
    public boolean isUsePercentValuesEnabled() {
        return mUsePercentValues;
    }

    public float getMaxAngle() {
        return mMaxAngle;
    }

    /**
     * Sets the max angle that is used for calculating the pie-circle. 360f means
     * it's a full PieChart, 180f results in a half-pie-chart. Default: 360f
     *
     * @param maxangle min 90, max 360
     */
    public void setMaxAngle(float maxangle) {

        if (maxangle > 360)
            maxangle = 360f;

        if (maxangle < 90)
            maxangle = 90f;

        this.mMaxAngle = maxangle;
    }

    /**
     * The minimum angle slices on the chart are rendered with, default is 0f.
     *
     * @return minimum angle for slices
     */
    public float getMinAngleForSlices() {
        return mMinAngleForSlices;
    }

    /**
     * Set the angle to set minimum size for slices, you must call {@link #notifyDataSetChanged()}
     * and {@link #invalidate()} when changing this, only works if there is enough room for all
     * slices to have the minimum angle.
     *
     * @param minAngle minimum 0, maximum is half of {@link #setMaxAngle(float)}
     */
    public void setMinAngleForSlices(float minAngle) {

        if (minAngle > (mMaxAngle / 2f))
            minAngle = mMaxAngle / 2f;
        else if (minAngle < 0)
            minAngle = 0f;

        this.mMinAngleForSlices = minAngle;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof PieChartRenderer) {
            ((PieChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
