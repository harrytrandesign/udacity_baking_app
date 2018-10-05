package com.htdwps.bakingappudacityproject.adapter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HTDWPS on 9/9/18.
 */
public class RetrofitClientManager {

    private static final String BAKING_RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        if (response.code() == 500) {

                            // Print response

                            return response;
                        }

                        return response;
                    }
                })
                .build();

        return okHttpClient;
    }

    public static Retrofit getClient() {

        return new Retrofit.Builder()
                .baseUrl(BAKING_RECIPE_BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

}
