package com.example.component_demo1.http;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * author: banketree
 * created on: 2019/6/12
 * description:
 * 天气预报
 */
public class WeatherApi extends ApiService {
    private final String key = "4e34d358d9aa062b2c46afd627084f85";
    private final String BASE_URL = "https://restapi.amap.com";

    @Override
    protected String getUrl() {
        return BASE_URL;
    }

    @Override
    protected Class<? extends Object> getService() {
        return WeatherServiceApi.class;
    }

    //添加头信息()
    @Override
    protected Response okhttpInterceptor(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder()
                .removeHeader("Content-Type")
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("charset", "UTF-8"); //登录前 必传

        Request request = builder.build();
        return chain.proceed(request);
    }

    //通过ip 获取城市编码
    private void getCityCodeByIp(HttpCallback callback) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("output", "json");
        hashMap.put("key", key);
        doNet("getCityCodeByIp", hashMap, callback);
    }

    //获取天气预报
    //步骤1：通过ip 获取城市信息
    //步骤2：通过城市编码 获取天气信息
    public void getWeather(final HttpCallback callback) {
        getCityCodeByIp(new HttpCallback<String>() {
            @Override
            public void onSucess(String respBase) {
                super.onSucess(respBase);
            }

            @Override
            public void onFaile(Exception ex) {
                super.onFaile(ex);
                if (callback != null) callback.onFaile(ex);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                if (callback != null) callback.onCancel();
            }

            @Override
            protected String getBean(retrofit2.Response response) throws JSONException {
                return super.getBean(response);
            }
        });
    }

    //    设置专属
    public interface WeatherServiceApi {
        /**
         * 通过ip 查城市信息
         */
        @GET("/v3/ip")
        Call<String> getCityCodeByIp(@QueryMap HashMap<String, String> map);
    }
}
