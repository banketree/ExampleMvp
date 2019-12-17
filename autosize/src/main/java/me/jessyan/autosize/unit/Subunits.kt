package me.jessyan.autosize.unit


import android.util.DisplayMetrics

/**
 * AndroidAutoSize 支持一些在 Android 系统上比较少见的单位作为副单位, 用于规避修改 [DisplayMetrics.density]
 * 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 */
enum class Subunits {
    /**
     * 不使用副单位
     */
    NONE,
    /**
     * 单位 pt
     *
     * @see android.util.TypedValue.COMPLEX_UNIT_PT
     */
    PT,
    /**
     * 单位 in
     *
     * @see android.util.TypedValue.COMPLEX_UNIT_IN
     */
    IN,
    /**
     * 单位 mm
     *
     * @see android.util.TypedValue.COMPLEX_UNIT_MM
     */
    MM
}
