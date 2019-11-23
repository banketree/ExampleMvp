package com.example.baselib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.example.baselib.base.delegate.IActivity;

import javax.inject.Inject;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@link Activity} 的三方库, 那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity}, 然后再按照 {@link BaseActivity} 的格式, 将代码复制过去, 记住一定要实现
 * ================================================
 */
//public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable {
//    protected final String TAG = this.getClass().getSimpleName();
//    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
//    private Cache<String, Object> mCache;
//    private Unbinder mUnbinder;
//    @Inject
//    @Nullable
//    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null
//
////    @NonNull
////    @Override
////    public synchronized Cache<String, Object> provideCache() {
////        if (mCache == null) {
////            mCache = ArmsUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
////        }
////        return mCache;
////    }
//
////    @NonNull
////    @Override
////    public final Subject<ActivityEvent> provideLifecycleSubject() {
////        return mLifecycleSubject;
////    }
//
////    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
////        View view = convertAutoView(name, context, attrs);
////        return view == null ? super.onCreateView(name, context, attrs) : view;
//        return null;
//    }
//
////    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        try {
////            int layoutResID = initView(savedInstanceState);
////            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
////            if (layoutResID != 0) {
////                setContentView(layoutResID);
////                //绑定到butterknife
////                mUnbinder = ButterKnife.bind(this);
////            }
////        } catch (Exception e) {
////            if (e instanceof InflateException) throw e;
////            e.printStackTrace();
////        }
////        initData(savedInstanceState);
//    }
//
////    @Override
//    protected void onDestroy() {
////        super.onDestroy();
////        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
////            mUnbinder.unbind();
////        this.mUnbinder = null;
////        if (mPresenter != null)
////            mPresenter.onDestroy();//释放资源
////        this.mPresenter = null;
//    }
//}
