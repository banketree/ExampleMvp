package com.example.component_demo1.http;

import android.content.Context;
import com.example.base_fun.http.LibHttpCallback;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Response;

public abstract class HttpCallback<T> extends LibHttpCallback {
    private Context context;

    public HttpCallback() {
    }

    public HttpCallback(String serviceName) {
        super(serviceName);
    }

    public HttpCallback(Context context, boolean showProgress) {
        this.context = context;
    }

    public HttpCallback(Context context, boolean showProgress, String serviceName) {
        super(serviceName);
        this.context = context;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (call.isCanceled()) {
            onCancel();
            return;
        }

        if (response == null || response.body() == null || (!response.isSuccessful())) {
            onFaile(new Exception("BNET404"));//网络请求异常
            return;
        }

        T t = null;
        try {
            t = getBean(response);
        } catch (Exception e) {
            t = null;
        }


        if (t == null) {
            onFaile(new Exception("net_inner_method")); //内部方法出错
            return;
        }
//            t.setServiceName(getServiceName());
//            if (t.isSuccess()) {
//                onSucess(t);
//                return;
//            }

        onFaile(new Exception("")); //服务器返回的错误
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if (call.isCanceled()) {
            onCancel();
            return;
        }

        onFaile(new Exception(t));
    }

    public void onSucess(T t) {
        dismiss();
    }

    public void onFaile(Exception ex) {
        dismiss();
    }

    public void onCancel() {
        dismiss();
    }

    protected T getBean(Response response) throws JSONException {
//        if (getType() == Companion.getTypeGson()) {
//            return ((Resp<T>) response.body()).getResponse();
//        } else if (getType() == Companion.getTypeXml()) {
//            return (T) response.body();
//        } else if (getType() == Companion.getTypeString()) {//需要重写方法
//        }

        return null;
    }

    public HttpCallback<T> setServiceName(String serviceName) {
        super.setServiceNameString(serviceName);
        return this;
    }

    public HttpCallback<T> asyn(boolean asyn) {
        super.setAsyn(asyn);
        return this;
    }

    public Context getContext() {
        return context;
    }

    public void dismiss() {
    }
}
