package com.ga.cloudfeed.data;


import android.support.annotation.NonNull;
import android.view.View;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.view.activity.MainActivity;
import com.ga.feedparser.Feed;
import com.ga.feedparser.FeedException;
import com.ga.feedparser.FeedParser;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class Parser {
    private static final String FEED_EXCEPTION = "Error, make sure it's a valid XML feed URL";
    private static final String IO_EXCEPTION = "Check your internet connection";
    private static final String ARGUMENT_EXCEPTION = "Invalid URL";
    private FeedToItemConverter converter;
    private OkHttpClient client;
    private FeedParser feedParser;

    @Inject
    public Parser(FeedToItemConverter converter, OkHttpClient client, FeedParser feedParser) {
        this.converter = converter;
        this.client = client;
        this.feedParser = feedParser;
    }

    public void parse(String url, boolean registered, boolean subscribed, FeedToItemConverter
            .ConverterCallback callback) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            MainActivity.showProgressBar(View.VISIBLE);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    CloudFeedApplication.showToast(IO_EXCEPTION);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try {
                        Feed feed = feedParser.parse(response.body().byteStream());
                        converter.convert(feed, callback, new HashMap<String, Object>() {{
                            put(BaseRepository.URL, url);
                            put(BaseRepository.REGISTERED, registered);
                            put(BaseRepository.SUBSCRIBED, subscribed);

                        }});
                    } catch (FeedException e) {
                        CloudFeedApplication.showToast(FEED_EXCEPTION);
                        e.printStackTrace();
                    }
                    MainActivity.showProgressBar(View.GONE);
                }
            });
        } catch (IllegalArgumentException e) {
            CloudFeedApplication.showToast(ARGUMENT_EXCEPTION);
        }
    }
}
