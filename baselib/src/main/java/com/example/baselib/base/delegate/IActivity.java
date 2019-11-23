package com.example.baselib.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.baselib.di.component.AppComponent;

/**
 * ================================================
 * 框架要求框架中的每个 {@link Activity} 都需要实现此类,以满足规范
 * ================================================
 */
public interface IActivity {

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    void setupActivityComponent(@NonNull AppComponent appComponent);

    /**
     * 是否使用 EventBus
     */
    boolean useEventBus();

    /**
     * 初始化 View
     */
    int initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initData(@Nullable Bundle savedInstanceState);
}
