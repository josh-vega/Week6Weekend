package com.example.week6weekend.views.activities;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.week6weekend.R;
import com.example.week6weekend.databinding.ActivityMainBinding;
import com.example.week6weekend.datasource.OkHttpHelper;
import com.example.week6weekend.model.Book;
import com.example.week6weekend.viewmodels.BookViewModel;
import com.example.week6weekend.views.adapters.RecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setBookViewModel(new BookViewModel(getApplication()));
        activityMainBinding.executePendingBindings();

        OkHttpHelper.asyncOkHttp("http://de-coding-test.s3.amazonaws.com/books.json");
    }

    @BindingAdapter({"bind:image_url"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(new RequestOptions().override(70, 70))
                .into(imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBooksEvent(ArrayList<Book> books){
        activityMainBinding.rvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(books);
        activityMainBinding.rvRecyclerView.setAdapter(recyclerViewAdapter);
    }
}
