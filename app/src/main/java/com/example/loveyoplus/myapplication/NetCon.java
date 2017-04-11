package com.example.loveyoplus.myapplication;


import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;


/**
 * Created by fatesaikou on 2017/3/14.
 */

public class NetCon {
    private OkHttpClient client = new OkHttpClient();
    RequestBody params;
    Request request;

    public NetCon SetJson(Map<String, String > data) {
        FormEncodingBuilder form = new FormEncodingBuilder();

        for (Map.Entry<String, String> entry: data.entrySet()) {
            form.add(entry.getKey(), entry.getValue());
        }

        params = form.build();

        return this;
    }

    public NetCon SetUrl(String url) throws IOException {
        if (params != null) {
            request = new Request.Builder().post(params).url(url).build();
            Log.e("request",request+" "+request.body().toString());

        } else {
            request = new Request.Builder().url(url).build();

        }

        return this;
    }

    public String Execute() throws IOException {
        Response response = client.newCall(request).execute();


        return response.body().string();
    }
}
