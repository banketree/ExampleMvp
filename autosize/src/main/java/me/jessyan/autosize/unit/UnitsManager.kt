package me.jessyan.autosize.unit

import android.util.DisplayMetrics
import me.jessyan.autosize.utils.Preconditions

/**
 * 管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
 * 其中 dp、sp 这两个是比较常见的单位, 作为 AndroidAutoSize 的主单位, 默认被 AndroidAutoSize 支持
 * pt、in、mm 这三个是比较少见的单位, 只可以选择其中的一个, 作为 AndroidAutoSize 的副单位, 与 dp、sp 一起被 AndroidAutoSize 支持
 * 副单位是用于规避修改 [DisplayMetrics.density] 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * 您选择什么单位, 就在 layout 文件中用什么单位布局
 */
class UnitsManager {
    /**
     * 设计图上的总宽度, 建议单位为 px, 当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时使用
     * 因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
     * 而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
     * 但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
     * 所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 就需要在 [UnitsManager] 中保存副单位的设计图尺寸
     */
    private var designWidth: Float = 0.toFloat()
    /**
     * 设计图上的总高度, 建议单位为 px, 当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时使用
     * 因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
     * 而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
     * 但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
     * 所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 就需要在 [UnitsManager] 中保存副单位的设计图尺寸
     */
    private var designHeight: Float = 0.toFloat()
    /**
     * 是否支持 dp 单位, 默认支持
     */
    private var isSupportDP = true
    /**
     * 是否支持 sp 单位, 默认支持
     */
    private var isSupportSP = true
    /**
     * 是否支持副单位, 以什么为副单位? 默认不支持
     */
    private var supportSubunits = Subunits.NONE
    /**
     * 是否支持 ScreenSizeDp 修改, 默认不支持
     */
    private var isSupportScreenSizeDP = false

    /**
     * 设置设计图尺寸
     *
     * @param designWidth  设计图上的总宽度, 建议单位为 px
     * @param designHeight 设计图上的总高度, 建议单位为 px
     * @return [UnitsManager]
     * @see .designWidth 详情请查看这个字段的注释
     *
     * @see .designHeight 详情请查看这个字段的注释
     */
    fun setDesignSize(designWidth: Float, designHeight: Float): UnitsManager {
        setDesignWidth(designWidth)
        setDesignHeight(designHeight)
        return this
    }

    /**
     * 返回 [.designWidth]
     *
     * @return [.designWidth]
     */
    fun getDesignWidth(): Float {
        return designWidth
    }

    /**
     * 设置设计图上的总宽度, 建议单位为 px
     *
     * @param designWidth 设计图上的总宽度, 建议单位为 px
     * @return [UnitsManager]
     * @see .designWidth 详情请查看这个字段的注释
     */
    fun setDesignWidth(designWidth: Float): UnitsManager {
        Preconditions.checkArgument(designWidth > 0, "designWidth must be > 0")
        this.designWidth = designWidth
        return this
    }

    /**
     * 返回 [.designHeight]
     *
     * @return [.designHeight]
     */
    fun getDesignHeight(): Float {
        return designHeight
    }

    /**
     * 设置设计图上的总高度, 建议单位为 px
     *
     * @param designHeight 设计图上的总高度, 建议单位为 px
     * @return [UnitsManager]
     * @see .designHeight 详情请查看这个字段的注释
     */
    fun setDesignHeight(designHeight: Float): UnitsManager {
        Preconditions.checkArgument(designHeight > 0, "designHeight must be > 0")
        this.designHeight = designHeight
        return this
    }

    /**
     * 是否支持 dp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    fun isSupportDP(): Boolean {
        return isSupportDP
    }

    /**
     * 是否让 AndroidAutoSize 支持 dp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportDP `true` 为支持, `false` 为不支持
     */
    fun setSupportDP(supportDP: Boolean): UnitsManager {
        isSupportDP = supportDP
        return this
    }

    /**
     * 是否支持 sp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    fun isSupportSP(): Boolean {
        return isSupportSP
    }

    /**
     * 是否让 AndroidAutoSize 支持 sp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportSP `true` 为支持, `false` 为不支持
     */
    fun setSupportSP(supportSP: Boolean): UnitsManager {
        isSupportSP = supportSP
        return this
    }

    /**
     * AndroidAutoSize 以什么单位为副单位, 默认为 [Subunits.NONE], 即不支持副单位, 详情请看类文件的注释 [UnitsManager]
     *
     * @return [Subunits]
     */
    fun getSupportSubunits(): Subunits {
        return supportSubunits
    }

    /**
     * 是否支持 ScreenSizeDp 修改, 默认不支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    fun isSupportScreenSizeDP(): Boolean {
        return isSupportScreenSizeDP
    }

    /**
     * 是否让 AndroidAutoSize 支持 ScreenSizeDp 修改, 默认不支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportScreenSizeDP `true` 为支持, `false` 为不支持
     */
    fun setSupportScreenSizeDP(supportScreenSizeDP: Boolean): UnitsManager {
        isSupportScreenSizeDP = supportScreenSizeDP
        return this
    }

    /**
     * 让 AndroidAutoSize 以什么单位为副单位, 在 pt、in、mm 这三个冷门单位中选择一个即可, 三个效果都是一样的
     * 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思
     * 默认为 [Subunits.NONE], 即不支持副单位, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportSubunits [Subunits]
     */
    fun setSupportSubunits(supportSubunits: Subunits): UnitsManager {
        this.supportSubunits = Preconditions.checkNotNull(
            supportSubunits,
            "The supportSubunits can not be null, use Subunits.NONE instead"
        )
        return this
    }
}
