package com.example.baselib.di.component;

import android.app.Application;
import com.example.baselib.base.delegate.AppDelegate;
import com.example.baselib.di.module.AppModule;
import com.example.baselib.di.module.GlobalConfigModule;
import com.example.baselib.di.module.HttpClientModule;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, HttpClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    //    /**
//     * 用于管理所有 {@link Activity}
//     * 之前 {@link AppManager} 使用 Dagger 保证单例, 只能使用 {@link AppComponent#appManager()} 访问
//     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 {@link AppManager#getAppManager()} 访问, 更加方便
//     * 但为了不影响之前使用 {@link AppComponent#appManager()} 获取 {@link AppManager} 的项目, 所以暂时保留这种访问方式
//     *
//     * @return {@link AppManager}
//     * @deprecated Use {@link AppManager#getAppManager()} instead
//     */
//    @Deprecated
//    AppManager appManager();
//
//    /**
//     * 用于管理网络请求层, 以及数据缓存层
//     *
//     * @return {@link IRepositoryManager}
//     */
//    IRepositoryManager repositoryManager();
//
//    /**
//     * RxJava 错误处理管理类
//     *
//     * @return {@link RxErrorHandler}
//     */
//    RxErrorHandler rxErrorHandler();
//
//    /**
//     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
//     * arms-imageloader-glide 提供 Glide 的策略实现类, 也可以自行实现
//     * 需要在 {@link ConfigModule#applyOptions(Context, GlobalConfigModule.Builder)} 中
//     * 手动注册 {@link BaseImageLoaderStrategy}, {@link ImageLoader} 才能正常使用
//     *
//     * @return
//     */
//    ImageLoader imageLoader();
//
//    /**
//     * 网络请求框架
//     *
//     * @return {@link OkHttpClient}
//     */
//    OkHttpClient okHttpClient();
//
//    /**
//     * Json 序列化库
//     *
//     * @return {@link Gson}
//     */
//    Gson gson();
//
//    /**
//     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
//     * 便于管理和清理, 可在 {@link ConfigModule#applyOptions(Context, GlobalConfigModule.Builder)} 种配置
//     *
//     * @return {@link File}
//     */
//    File cacheFile();
//
//    /**
//     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 {@link Application} 的生命周期一致
//     *
//     * @return {@link Cache}
//     */
//    Cache<String, Object> extras();
//
//    /**
//     * 用于创建框架所需缓存对象的工厂
//     *
//     * @return {@link Cache.Factory}
//     */
//    Cache.Factory cacheFactory();
//
//    /**
//     * 返回一个全局公用的线程池,适用于大多数异步需求。
//     * 避免多个线程池创建带来的资源消耗。
//     *
//     * @return {@link ExecutorService}
//     */
//    ExecutorService executorService();
//
    void inject(AppDelegate delegate);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder globalConfigModule(GlobalConfigModule globalConfigModule);

        AppComponent build();
    }
}
