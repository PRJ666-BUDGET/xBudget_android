package com.prj666_183a06.xbudget;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class MyColorTemplate {
    /**
     * an "invalid" color that indicates that no color is set
     */
    public static final int COLOR_NONE = 0x00112233;

    /**
     * this "color" is used for the Legend creation and indicates that the next
     * form should be skipped
     */
    public static final int COLOR_SKIP = 0x00112234;

    /**
     * THE COLOR THEMES ARE PREDEFINED (predefined color integer arrays), FEEL
     * FREE TO CREATE YOUR OWN WITH AS MANY DIFFERENT COLORS AS YOU WANT
     */
    public static final int[] LIBERTY_COLORS = {
            Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
    };
    public static final int[] JOYFUL_COLORS = {
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    };
    public static final int[] PASTEL_COLORS = {
            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
    };
    public static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };
    public static final int[] VORDIPLOM_COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
    };
    public static final int[] MATERIAL_COLORS = {
            rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
    };

    public static final int[] COOL_COLORS = {
            Color.rgb(65, 179, 247), Color.rgb(89, 219, 241), Color.rgb(115, 235, 174), Color.rgb(101, 168, 196), Color.rgb(154, 147, 236),
            Color.rgb(129, 203, 248), Color.rgb(158, 231, 250), Color.rgb(181, 249, 211), Color.rgb(170, 206, 226), Color.rgb(202, 185, 241),
            Color.rgb(0, 173, 206), Color.rgb(0, 197, 144), Color.rgb(0, 65, 89), Color.rgb(140, 101, 211), Color.rgb(0, 82, 165)
    };

    public static final int[] BEACH_COLORS = {
            Color.rgb(177, 221, 161), Color.rgb(60, 214, 230), Color.rgb(6, 194, 244),Color.rgb(255, 134, 66), Color.rgb(195, 183, 172),
            Color.rgb(229, 243, 207), Color.rgb(151, 234, 244), Color.rgb(95, 216, 250), Color.rgb(244, 220, 181), Color.rgb(231, 227, 215),
            Color.rgb(229, 243, 207), Color.rgb(151, 234, 244), Color.rgb(195, 54, 44), Color.rgb(129, 108, 91), Color.rgb(102, 141, 60)
    };

    public static final int[] CUTE_COLORS = {
            Color.rgb(201, 147, 212), Color.rgb(141, 182, 199), Color.rgb(202, 159, 146), Color.rgb(177, 194, 122), Color.rgb(89, 173, 208),
            Color.rgb(209, 141, 178), Color.rgb(193, 179, 142), Color.rgb(249, 205, 151), Color.rgb(178, 226, 137), Color.rgb(112, 149, 225),
            Color.rgb(241, 195, 208), Color.rgb(209, 198, 191), Color.rgb(227, 217, 176), Color.rgb(81, 192, 191), Color.rgb(159, 163, 227)
    };

    public static final int[] EARTH_COLORS = {
            Color.rgb(219, 202, 105), Color.rgb(189, 208, 156), Color.rgb(163, 173, 184), Color.rgb(169, 161, 140), Color.rgb(185, 156, 107),
            Color.rgb(213, 117, 0), Color.rgb(102, 141, 60), Color.rgb(131, 146, 159), Color.rgb(129, 108, 91), Color.rgb(133, 87, 35),
            Color.rgb(143, 59, 27), Color.rgb(64, 79, 36), Color.rgb(78, 97, 114), Color.rgb(73, 56, 41), Color.rgb(97, 52, 24)
    };

    public static final int[] BEAUTIFUL_COLORS = {
            Color.rgb(255, 169, 206), Color.rgb(173, 222, 250), Color.rgb(204, 255, 0), Color.rgb(108, 92, 50), Color.rgb(255, 202, 43),
            Color.rgb(241, 80, 149), Color.rgb(80, 168, 227), Color.rgb(184, 214, 2), Color.rgb(127, 66, 51), Color.rgb(244, 101, 40),
            Color.rgb(238, 47, 127), Color.rgb(64, 120, 211), Color.rgb(127, 176, 5), Color.rgb(124, 20, 77), Color.rgb(237, 2, 11)
    };

    public static final int[] WARM_COLORS = {
            Color.rgb(238, 197, 169), Color.rgb(199, 195, 151), Color.rgb(231, 227, 181), Color.rgb(211, 201, 206), Color.rgb(195, 142, 99),
            Color.rgb(229, 174, 134), Color.rgb(157, 151, 84), Color.rgb(223, 210, 124), Color.rgb(183, 166, 173), Color.rgb(172, 11, 61),
            Color.rgb(228, 153, 105), Color.rgb(110, 118, 73), Color.rgb(180,  168, 81), Color.rgb(132, 109, 116), Color.rgb(121, 63, 13)
    };



    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    /**
     * Returns the Android ICS holo blue light color.
     *
     * @return
     */
    public static int getHoloBlue() {
        return Color.rgb(51, 181, 229);
    }

    /**
     * Sets the alpha component of the given color.
     *
     * @param color
     * @param alpha 0 - 255
     * @return
     */
    public static int colorWithAlpha(int color, int alpha) {
        return (color & 0xffffff) | ((alpha & 0xff) << 24);
    }

    /**
     * turn an array of resource-colors (contains resource-id integers) into an
     * array list of actual color integers
     *
     * @param r
     * @param colors an integer array of resource id's of colors
     * @return
     */
    public static List<Integer> createColors(Resources r, int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(r.getColor(i));
        }

        return result;
    }

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     *
     * @param colors
     * @return
     */
    public static List<Integer> createColors(int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(i);
        }

        return result;
    }
}
