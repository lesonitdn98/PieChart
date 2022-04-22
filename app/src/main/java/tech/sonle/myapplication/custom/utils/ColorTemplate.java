
package tech.sonle.myapplication.custom.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds predefined color integer arrays (e.g.
 * ColorTemplate.VORDIPLOM_COLORS) and convenience methods for loading colors
 * from resources.
 */
public class ColorTemplate {

    /**
     * an "invalid" color that indicates that no color is set
     */
    public static final int COLOR_NONE = 0x00112233;

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     */
    public static List<Integer> createColors(int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(i);
        }

        return result;
    }
}
