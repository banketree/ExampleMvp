package me.jessyan.autosize.external;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {用来存储外部三方库的适配参数, 因为 AndroidAutoSize 默认会对项目中的所有模块都进行适配
 */
public class ExternalAdaptInfo implements Parcelable {
    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选一个作为基准进行适配)
     * {@code true} 为按照宽度适配, {@code false} 为按照高度适配
     */
    private boolean isBaseOnWidth;
    /**
     * 设计图上的设计尺寸, 单位 dp (三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试)
     * {@link #sizeInDp} 须配合 {@link #isBaseOnWidth} 使用, 规则如下:
     * 如果 {@link #isBaseOnWidth} 设置为 {@code true}, {@link #sizeInDp} 则应该设置为设计图的总宽度
     * 如果 {@link #isBaseOnWidth} 设置为 {@code false}, {@link #sizeInDp} 则应该设置为设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, {@link #sizeInDp} 则设置为 {@code 0}
     */
    private float sizeInDp;

    public ExternalAdaptInfo(boolean isBaseOnWidth) {
        this.isBaseOnWidth = isBaseOnWidth;
    }

    public ExternalAdaptInfo(boolean isBaseOnWidth, float sizeInDp) {
        this.isBaseOnWidth = isBaseOnWidth;
        this.sizeInDp = sizeInDp;
    }

    public boolean isBaseOnWidth() {
        return isBaseOnWidth;
    }

    public void setBaseOnWidth(boolean baseOnWidth) {
        isBaseOnWidth = baseOnWidth;
    }

    public float getSizeInDp() {
        return sizeInDp;
    }

    public void setSizeInDp(float sizeInDp) {
        this.sizeInDp = sizeInDp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isBaseOnWidth ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.sizeInDp);
    }

    protected ExternalAdaptInfo(Parcel in) {
        this.isBaseOnWidth = in.readByte() != 0;
        this.sizeInDp = in.readFloat();
    }

    public static final Creator<ExternalAdaptInfo> CREATOR = new Creator<ExternalAdaptInfo>() {
        @Override
        public ExternalAdaptInfo createFromParcel(Parcel source) {
            return new ExternalAdaptInfo(source);
        }

        @Override
        public ExternalAdaptInfo[] newArray(int size) {
            return new ExternalAdaptInfo[size];
        }
    };

    @Override
    public String toString() {
        return "ExternalAdaptInfo{" +
                "isBaseOnWidth=" + isBaseOnWidth +
                ", sizeInDp=" + sizeInDp +
                '}';
    }
}
