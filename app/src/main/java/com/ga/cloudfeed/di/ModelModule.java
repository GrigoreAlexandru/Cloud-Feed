package com.ga.cloudfeed.di;

import android.arch.lifecycle.ViewModelProvider;

import com.ga.cloudfeed.data.FeedToItemConverter;
import com.ga.cloudfeed.data.Parser;
import com.ga.cloudfeed.view.adapter.GenericAdapter;
import com.ga.feedparser.FeedParser;
import com.ga.feedparser.FeedParserFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ModelModule {

    @Provides
    FeedParser provideFeedParser(){return FeedParserFactory.newParser();}

    @Provides
    Parser provideParser(FeedToItemConverter converter, OkHttpClient client, FeedParser feedParser){
        return new Parser(converter, client, feedParser);
    }

    @Provides
    FeedToItemConverter provideFeedToItemConverter(){
        return new FeedToItemConverter();
    }

    @Provides
    GenericAdapter provideItemAdapter(ViewModelProvider.Factory factory){
        return new GenericAdapter(factory);
    }
}
