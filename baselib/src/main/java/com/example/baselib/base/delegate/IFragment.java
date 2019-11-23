package com.example.baselib.base.delegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.baselib.di.component.AppComponent;

/**
 * ================================================
 * 框架要求框架中的每个 Fragment 都需要实现此类,以满足规范
 * ================================================
 */
public interface IFragment {

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    void setupFragmentComponent(@NonNull AppComponent appComponent);

    /**
     * 是否使用 EventBus
     */
    boolean useEventBus();

    /**
     * 初始化 View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(@Nullable Bundle savedInstanceState);
}
