package me.jessyan.autosize;

import android.app.Activity;

/**
 * {@link AutoAdaptStrategy} 的包装者, 用于给 {@link AutoAdaptStrategy} 的实现类增加一些额外的职责
 */
public class WrapperAutoAdaptStrategy implements AutoAdaptStrategy {
    private final AutoAdaptStrategy mAutoAdaptStrategy;

    public WrapperAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void applyAdapt(Object target, Activity activity) {
        onAdaptListener onAdaptListener = AutoSizeConfig.getInstance().getOnAdaptListener();
        if (onAdaptListener != null){
            onAdaptListener.onAdaptBefore(target, activity);
        }
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(target, activity);
        }
        if (onAdaptListener != null){
            onAdaptListener.onAdaptAfter(target, activity);
        }
    }
}
