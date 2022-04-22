
package tech.sonle.myapplication.custom.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

import tech.sonle.myapplication.custom.utils.Utils;

/**
 * Class representing one entry in the chart. Might contain multiple values.
 * Might only contain a single value depending on the used constructor.
 */
public class Entry extends BaseEntry {

    /**
     * the x value
     */
    private float x = 0f;

    public Entry() {

    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x the x value
     * @param y the y value (the actual value of the entry)
     */
    public Entry(float x, float y) {
        super(y);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x    the x value
     * @param y    the y value (the actual value of the entry)
     * @param color Color.
     */
    public Entry(float x, float y, int color) {
        super(y, color);
        this.x = x;
    }

    /**
     * A Entry represents one single entry in the chart.
     *
     * @param x    the x value
     * @param y    the y value (the actual value of the entry)
     * @param icon icon image
     */
    public Entry(float x, float y, Drawable icon) {
        super(y, icon);
        this.x = x;
    }

    /**
     * Returns the x-value of this Entry object.
     *
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-value of this Entry object.
     *
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * returns an exact copy of the entry
     *
     * @return
     */
    public Entry copy() {
        return new Entry(x, getY(), getColor());
    }

    /**
     * Compares value, xIndex and data of the entries. Returns true if entries
     * are equal in those points, false if not. Does not check by hash-code like
     * it's done by the "equals" method.
     *
     * @param e
     * @return
     */
    public boolean equalTo(Entry e) {

        if (e == null)
            return false;

        if (e.getColor() != this.getColor())
            return false;

        if (Math.abs(e.x - this.x) > Utils.Companion.getFLOAT_EPSILON())
            return false;

        if (Math.abs(e.getY() - this.getY()) > Utils.Companion.getFLOAT_EPSILON())
            return false;

        return true;
    }

    /**
     * returns a string representation of the entry containing x-index and value
     */
    @Override
    public String toString() {
        return "Entry, x: " + x + " y: " + getY();
    }
}
