package com.example.baselib.base.delegate;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;

/**
 * ================================================
 * 用于代理应用的生命周期
 * ================================================
 */
public interface AppLifecycles {
    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}
