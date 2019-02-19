package com.example.week6weekend.datasource;

import android.util.Log;

import com.example.week6weekend.model.Book;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpHelper {

    public static void asyncOkHttp(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            String jsonResponse;
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonResponse = response.body().string();
                Gson gson = new Gson();
                Book[] books = gson.fromJson(jsonResponse, Book[].class);
                List<Book> bookList = Arrays.asList(books);
                ArrayList<Book> bookArrayList = new ArrayList<>(bookList);
                if(bookArrayList != null){
                    EventBus.getDefault().post(bookArrayList);
                } else {
                    Log.d("TAG", "onResponse: Books could not be retrieved.");
                }
            }
        });
    }

}
