package com.example.baselib.di.module;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

//    @Singleton
//    @Provides
//    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
//        GsonBuilder builder = new GsonBuilder();
//        if (configuration != null)
//            configuration.configGson(application, builder);
//        return builder.create();
//    }
//
//    /**
//     * 之前 {@link AppManager} 使用 Dagger 保证单例, 只能使用 {@link AppComponent#appManager()} 访问
//     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 {@link AppManager#getAppManager()} 访问, 更加方便
//     * 但为了不影响之前使用 {@link AppComponent#appManager()} 获取 {@link AppManager} 的项目, 所以暂时保留这种访问方式
//     *
//     * @param application
//     * @return
//     */
//    @Singleton
//    @Provides
//    static AppManager provideAppManager(Application application) {
//        return AppManager.getAppManager().init(application);
//    }
//
//    @Binds
//    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);
//
//    @Singleton
//    @Provides
//    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
//        return cacheFactory.build(CacheType.EXTRAS);
//    }
//
//    @Binds
//    @Named("ActivityLifecycle")
//    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);
//
//    @Binds
//    @Named("ActivityLifecycleForRxLifecycle")
//    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle);
//
//    @Binds
//    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);
//
//    @Singleton
//    @Provides
//    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
//        return new ArrayList<>();
//    }
//
//    public interface GsonConfiguration {
//        void configGson(@NonNull Context context, @NonNull GsonBuilder builder);
//    }
}
