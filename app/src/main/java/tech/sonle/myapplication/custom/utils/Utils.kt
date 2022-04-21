package tech.sonle.myapplication.custom.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import tech.sonle.myapplication.custom.formatter.DefaultValueFormatter
import tech.sonle.myapplication.custom.formatter.IValueFormatter
import kotlin.math.ceil
import kotlin.math.log10

/**
 * Create by SonLe on 19/04/2022
 */
abstract class Utils {
    companion object {
        private var mMetrics: DisplayMetrics? = null
        private var mMinimumFlingVelocity = 50
        private var mMaximumFlingVelocity = 8000

        const val DEG2RAD = Math.PI / 180.0
        const val FDEG2RAD = Math.PI.toFloat() / 180f

        val FLOAT_EPSILON = java.lang.Float.intBitsToFloat(1)

        /**
         * initialize method, called inside the Chart.init() method.
         *
         * @param context
         */
        fun init(context: Context?) {
            if (context == null) {
                // noinspection deprecation
                mMinimumFlingVelocity =
                    ViewConfiguration.getMinimumFlingVelocity()
                // noinspection deprecation
                mMaximumFlingVelocity =
                    ViewConfiguration.getMaximumFlingVelocity()
                Log.e(
                    "MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL"
                )
            } else {
                val viewConfiguration = ViewConfiguration.get(context)
                mMinimumFlingVelocity =
                    viewConfiguration.scaledMinimumFlingVelocity
                mMaximumFlingVelocity =
                    viewConfiguration.scaledMaximumFlingVelocity
                val res = context.resources
                mMetrics = res.displayMetrics
            }
        }

        /**
         * This method converts dp unit to equivalent pixels, depending on device
         * density. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
         *
         * @param dp A value in dp (density independent pixels) unit. Which we need
         * to convert into pixels
         * @return A float value to represent px equivalent to dp depending on
         * device density
         */
        fun convertDpToPixel(dp: Float): Float {
            if (mMetrics == null) {
                Log.e(
                    "MPChartLib-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                            " calling Utils.convertDpToPixel(...). Otherwise conversion does not " +
                            "take place."
                )
                return dp
            }
            return dp * mMetrics!!.density
        }

        /**
         * calculates the approximate width of a text, depending on a demo text
         * avoid repeated calls (e.g. inside drawing methods)
         *
         * @param paint
         * @param demoText
         * @return
         */
        fun calcTextWidth(paint: Paint, demoText: String?): Int {
            return paint.measureText(demoText).toInt()
        }

        private val mCalcTextHeightRect = Rect()

        /**
         * calculates the approximate height of a text, depending on a demo text
         * avoid repeated calls (e.g. inside drawing methods)
         *
         * @param paint
         * @param demoText
         * @return
         */
        fun calcTextHeight(paint: Paint, demoText: String): Int {
            val r = mCalcTextHeightRect
            r[0, 0, 0] = 0
            paint.getTextBounds(demoText, 0, demoText.length, r)
            return r.height()
        }

        private val mDefaultValueFormatter: IValueFormatter = generateDefaultValueFormatter()

        private fun generateDefaultValueFormatter(): IValueFormatter {
            return DefaultValueFormatter(1)
        }

        /// - returns: The default value formatter used for all chart components that needs a default
        fun getDefaultValueFormatter(): IValueFormatter? {
            return mDefaultValueFormatter
        }

        /**
         * Original method view.postInvalidateOnAnimation() only supportd in API >=
         * 16, This is a replica of the code from ViewCompat.
         *
         * @param view
         */
        @SuppressLint("NewApi")
        fun postInvalidateOnAnimation(view: View) {
            view.postInvalidateOnAnimation()
        }

        /**
         * returns an angle between 0.f < 360.f (not less than zero, less than 360)
         */
        fun getNormalizedAngle(angle: Float): Float {
            var angle = angle
            while (angle < 0f) angle += 360f
            return angle % 360f
        }

        fun getSDKInt(): Int {
            return Build.VERSION.SDK_INT
        }

        /**
         * rounds the given number to the next significant number
         *
         * @param number
         * @return
         */
        fun roundToNextSignificant(number: Double): Float {
            if (java.lang.Double.isInfinite(number) ||
                java.lang.Double.isNaN(number) || number == 0.0
            ) return 0F
            val d = ceil(log10(if (number < 0) -number else number))
                .toFloat()
            val pw = 1 - d.toInt()
            val magnitude = Math.pow(10.0, pw.toDouble()).toFloat()
            val shifted = Math.round(number * magnitude)
            return shifted / magnitude
        }

        /**
         * Returns the appropriate number of decimals to be used for the provided
         * number.
         *
         * @param number
         * @return
         */
        fun getDecimals(number: Float): Int {
            val i: Float =
                roundToNextSignificant(number.toDouble())
            return if (java.lang.Float.isInfinite(i)) 0 else ceil(-log10(i.toDouble()))
                .toInt() + 2
        }
    }
}