package com.water.eldowas.api.interceptor;


import androidx.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.water.eldowas.BuildConfig;
import com.water.eldowas.api.ApiClient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class add information an authorization key to {@link okhttp3.OkHttpClient} which is passed in
 * {@link ApiClient#getRestAdapter()} which is required when making a request.
 *
 * @author Thomas Kioko
 */
public class AccessTokenIterceptor implements Interceptor {

    public AccessTokenIterceptor() {

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

       String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;

        Log.e("AccessToken", keys);

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);
    }
}
