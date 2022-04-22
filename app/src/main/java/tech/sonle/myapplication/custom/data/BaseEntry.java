package tech.sonle.myapplication.custom.data;

import android.graphics.drawable.Drawable;

import tech.sonle.myapplication.custom.utils.ColorTemplate;

/**
 * Created by Philipp Jahoda on 02/06/16.
 */
public abstract class BaseEntry {

    /** the y value */
    private float y = 0f;

    /** optional icon image */
    private Drawable mIcon = null;

    /** optional color */
    private int mColor = ColorTemplate.COLOR_NONE;

    public BaseEntry() {

    }

    public BaseEntry(float y) {
        this.y = y;
    }

    public BaseEntry(float y, int color) {
        this(y);
        this.mColor = color;
    }

    public BaseEntry(float y, Drawable icon) {
        this(y);
        this.mIcon = icon;
    }

    /**
     * Returns the y value of this Entry.
     *
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the icon drawable
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    /**
     * Returns the icon of this Entry.
     *
     * @return
     */
    public Drawable getIcon() {
        return mIcon;
    }

    /**
     * Sets the y-value for the Entry.
     *
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    public int getColor() { return mColor; }
}
