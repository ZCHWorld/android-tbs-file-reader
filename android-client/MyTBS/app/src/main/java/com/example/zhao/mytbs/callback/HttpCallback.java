package com.example.zhao.mytbs.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;

public abstract class HttpCallback<T> {
    public abstract void onSuccess(T result);
    public void onError(Request request, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    };
}
