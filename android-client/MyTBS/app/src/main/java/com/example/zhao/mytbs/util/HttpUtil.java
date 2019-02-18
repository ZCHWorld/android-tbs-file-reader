package com.example.zhao.mytbs.util;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.carrier.CarrierMessagingService;

import com.example.zhao.mytbs.callback.HttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String GET = "get";
    private static final String POST = "post";

    private static HttpUtil mInstance;
    private OkHttpClient client;
    private Handler mDelivery;//回调handler

    private HttpUtil() {
        client = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;
    }

    public static void get(String url, HttpCallback callback) {
        getInstance().handle(GET, url, null, callback);
    }

    public static void post(String url, String json, HttpCallback callback) {
        getInstance().handle(POST, url, json, callback);
    }

    public void handle(String method, String url, String json, final HttpCallback callback) {
        Request request = null;
        switch (method) {
            case GET:
                request = new Request.Builder().url(url).build();
                break;
            case POST:
                RequestBody body = RequestBody.create(JSON, json);
                request = new Request.Builder().url(url).post(body).build();
                break;
            default:
                break;
        }
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                if (response.isSuccessful()){
                    sendSuccessResultCallback(response.body().string(), callback);
                } else {
                    sendFailedStringCallback(call, null, callback);
                }
                Looper.loop();
            }
        });
    }
    private void sendFailedStringCallback(final Call call, final Exception e, final HttpCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(call.request(), e);


            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final HttpCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(object);
                }
            }
        });
    }
    public static void main(String[] args) throws IOException {
    }
}
