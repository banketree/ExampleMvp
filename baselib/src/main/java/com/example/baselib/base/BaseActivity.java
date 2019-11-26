package com.example.baselib.base;

import android.os.Bundle;
import android.view.InflateException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.baselib.base.delegate.IActivity;
import com.example.baselib.integration.lifecycle.ActivityLifecycleable;
import com.example.baselib.mvp.IPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import javax.inject.Inject;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@Activity} 的三方库, 那你就需要自己自定义 {@@Activity}
 * 继承于这个特定的 {@Activity}, 然后再按照 {@BaseActivity} 的格式, 将代码复制过去, 记住一定要实现
 * ================================================
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    //    private Cache<String, Object> mCache;
    private Unbinder unbinder;

    @Inject
    @Nullable
    protected P presenter;//如果当前页面逻辑简单, Presenter 可以为 null

//    @NonNull
//    @Override
//    public synchronized Cache<String, Object> provideCache() {
//        if (mCache == null) {
//            mCache = MvpUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
//        }
//        return mCache;
//    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return lifecycleSubject;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                unbinder = ButterKnife.bind(this);//绑定到butterknife
            }
        } catch (Exception e) {
            if (e instanceof InflateException) throw e;
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY)
            unbinder.unbind();
        this.unbinder = null;
        if (presenter != null)
            presenter.onDestroy();//释放资源
        this.presenter = null;
    }
}
