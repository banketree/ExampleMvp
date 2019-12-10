package com.example.base_fun.http;

import androidx.annotation.NonNull;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LibHttpService {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public OkHttpClient httpClient = null;
    private HashMap<String, WeakReference<Call>> callMap = new HashMap<>();
    private volatile boolean isDestory = false;

    protected String getUrl() {
        return "";
    }

    protected Class<? extends Object> getService() {
        return Object.class;
    }

    protected void resetOkHttpClient() {
        httpClient = null;
    }

    protected OkHttpClient getOkHttpClient() {
        if (httpClient == null) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = new UnSafeTrustManager();
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();

            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            OkHttpClient.Builder builder = new OkHttpClient()
                    .newBuilder();
            initUrlManager(builder);
            httpClient = builder
                    .retryOnConnectionFailure(false)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new UnSafeHostnameVerifier())
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            return okhttpInterceptor(chain);
                        }
                    })
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .removeHeader("Content-Type")
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
//                                    .addHeader("Accept-Encoding", "gzip, deflate")
//                                    .addHeader("Connection", "keep-alive")
//                                    .addHeader("Accept", "*/*")
//                                    .addHeader("Cookie", "add cookies here")
                                    .build();
                            okhttp3.Response response = chain.proceed(request);
                            return response.newBuilder()
                                    .removeHeader("Pragma")
                                    .removeHeader("Cache-Control")
                                    .build();
                        }
                    })
                    .cache(null)
                    .build();
        }
        return httpClient;
    }

    protected void initUrlManager(OkHttpClient.Builder builder) {
        if (builder == null) return;
        // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
        RetrofitUrlManager.getInstance().with(builder);
    }

    protected okhttp3.Response okhttpInterceptor(Interceptor.Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .removeHeader("Content-Type")
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
//                                    .addHeader("Accept-Encoding", "gzip, deflate")
//                                    .addHeader("gitConnection", "keep-alive")
//                                    .addHeader("Accept", "*/*")
//                                    .addHeader("Cookie", "add cookies here")
//                .removeHeader("version")
//                .addHeader("version",AppContextUtils.getAppInfo())
                .build();
        return chain.proceed(request);
    }

    //gson
    private synchronized Object gsonService() {
        return new Retrofit.Builder()
                .baseUrl(getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build().create(getService());
    }

    //String
    private synchronized Object stringService() {
        return new Retrofit.Builder()
                .baseUrl(getUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build().create(getService());
    }

    //xml
//    private synchronized Object xmlService() {
//        return new Retrofit.Builder()
//                .baseUrl(getUrl())
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(getOkHttpClient())
//                .build().create(getService());
//    }

    /*
     * 异步
     * hashMap 请求参数(ParamUtil.getParam(svceName, dvcCode, data, encryptKey))
     * callback 回调方法
     * */
    protected void asynNetGson(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
        asynNetGson(hashMap, new LibHttpCallback(serverName) {
        });
    }

    protected void asynNetGson(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
        try {
            callback.setType(LibHttpCallback.Companion.getTypeGson());
            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
            Call call = (Call) method.invoke(gsonService(), hashMap);
            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
            callback.setCall(call);
            call.enqueue(callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        }
    }

    /*
     * 同步
     * hashMap 请求参数(ParamUtil.getParam(svceName, dvcCode, data, encryptKey))
     * String 服务名
     * */
    protected void synNetGson(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
        synNetGson(hashMap, new LibHttpCallback(serverName) {
        });
    }

    /*
     * 同步
     * hashMap 请求参数(ParamUtil.getParam(svceName, dvcCode, data, encryptKey))
     * callback 回调方法
     * */
    protected void synNetGson(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
        try {
            callback.setType(LibHttpCallback.Companion.getTypeGson());
            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
            Call call = (Call) method.invoke(gsonService(), hashMap);
            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
            callback.setCall(call);
            Response response = call.execute();
            if (response.isSuccessful()) {
                callback.onResponse(call, response);
            } else {
                callback.onFailure(call, new Exception(response.message()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        }
    }

    /*
     *异步
     * hashMap 请求参数
     * callback 回调方法
     * */
    protected void asynNetString(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
        asynNetString(hashMap, new LibHttpCallback(serverName) {
        });
    }

    protected void asynNetString(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
        try {
            callback.setType(LibHttpCallback.Companion.getTypeString());
            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
            Call call = (Call) method.invoke(stringService(), hashMap);
            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
            callback.setCall(call);
            call.enqueue(callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        }
    }

    /*
     * 同步
     * hashMap 请求参数
     * callback 回调方法
     * */
    protected void synNetString(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
        synNetString(hashMap, new LibHttpCallback(serverName) {
        });
    }

    protected void synNetString(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
        try {
            callback.setType(LibHttpCallback.Companion.getTypeString());
            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
            Call call = (Call) method.invoke(stringService(), hashMap);
            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
            callback.setCall(call);
            Response response = call.execute();
            if (response.isSuccessful()) {
                callback.onResponse(call, response);
            } else {
                callback.onFailure(call, new Exception(response.message()));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception("net_inner_method");
        }
    }

//    /*
//     *异步
//     * hashMap 请求参数
//     * callback 回调方法
//     * */
//    protected void asynNetXml(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
//        asynNetXml(hashMap, new LibHttpCallback(serverName) {
//        });
//    }
//
//    protected void asynNetXml(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
//        try {
//            callback.setType(LibHttpCallback.Companion.getTypeXml());
//            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
//            Call call = (Call) method.invoke(xmlService(), hashMap);
//            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
//            callback.setCall(call);
//            call.enqueue(callback);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        }
//    }

//    /*
//     *同步
//     * hashMap 请求参数
//     * callback 回调方法
//     * */
//    protected void synNetXml(@NonNull HashMap<String, String> hashMap, @NonNull String serverName) throws Exception {
//        synNetXml(hashMap, new LibHttpCallback(serverName) {
//        });
//    }
//
//    protected void synNetXml(@NonNull HashMap<String, String> hashMap, @NonNull LibHttpCallback callback) throws Exception {
//        try {
//            callback.setType(LibHttpCallback.Companion.getTypeXml());
//            Method method = getService().getDeclaredMethod(callback.getServiceName(), HashMap.class);
//            Call call = (Call) method.invoke(xmlService(), hashMap);
//            callMap.put(callback.getServiceName(), new WeakReference<Call>(call));
//            callback.setCall(call);
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                callback.onResponse(call, response);
//            } else {
//                callback.onFailure(call, new Exception(response.message()));
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            throw new Exception("net_inner_method");
//        }
//    }

    public Call getCall(String serverName) {
        if (callMap == null || callMap.isEmpty() || !callMap.containsKey(serverName))
            return null;
        return callMap.get(serverName).get();
    }

    public void destory() {
        isDestory = true;
        if (callMap == null || callMap.isEmpty())
            return;
        Iterator iter = callMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            WeakReference<Call> call = (WeakReference<Call>) entry.getValue();
            if (call != null && call.get() != null) {
                call.get().cancel();
            }
        }
    }

    public boolean isDestory() {
        return isDestory;
    }

    public static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
