package com.ga.cloudfeed.di;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.data.ItemRepository;
import com.ga.cloudfeed.data.Parser;
import com.ga.cloudfeed.data.database.FeedDao;
import com.ga.cloudfeed.data.database.FeedDatabase;
import com.ga.cloudfeed.data.database.ItemDao;
import com.ga.cloudfeed.viewmodel.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private final FeedDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                FeedDatabase.class,
                "ItemModel.db"
        ).build();
    }

    @Provides
    @Singleton
    FeedRepository provideFeedRepository(FeedDao dao){return new FeedRepository(dao);}

    @Provides
    @Singleton
    ItemRepository provideItemRepository(ItemDao dao, Parser parser, FeedRepository feedRepository){
        return new ItemRepository(dao, parser, feedRepository);
    }

    @Provides
    @Singleton
    ItemDao provideItemDao(FeedDatabase database){
        return database.itemDao();
    }

    @Provides
    @Singleton
    FeedDao provideFeedDao(FeedDatabase database){
        return database.feedDao();
    }

    @Provides
    @Singleton
    FeedDatabase provideFeedDatabase(){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(ItemRepository itemRepository,
                                                      FeedRepository feedRepository){
        return new CustomViewModelFactory(itemRepository, feedRepository);
    }
}
