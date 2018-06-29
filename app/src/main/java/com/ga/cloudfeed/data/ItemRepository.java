package com.ga.cloudfeed.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ga.cloudfeed.data.database.ItemDao;
import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.ItemModel;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ItemRepository extends BaseRepository<ItemModel, ItemDao> {
    private final Parser parser;
    private final FeedRepository feedRepository;

    public ItemRepository(ItemDao dao, Parser parser, FeedRepository feedRepository) {
        super(dao);
        this.parser = parser;
        this.feedRepository = feedRepository;
    }

    public LiveData<List<ItemModel>> getByDateDesc() {
        return dao.getByDateDesc();
    }

    public LiveData<List<ItemModel>> getByReadLater() {
        return dao.getByReadLater();
    }

    public LiveData<List<ItemModel>> getByFeed(String feedLink) {
        return dao.getByFeed(feedLink);
    }

    public ItemModel getOneByFeed(String feedLink) {
        try {
            return new GetOneTask().execute(feedLink).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setReadLater(String link) {
        new UpdateTask(dao).execute(READ_LATER, link);
    }

    public void setRead(String link) {
        new UpdateTask(dao).execute(READ, link);
    }


    public void parseAndInsert(String url, boolean registered, boolean subscribed) {
        parser.parse(url, registered, subscribed, this::insertFeedWithItems);
    }

    public void insertFeedWithItems(FeedModel feed, ItemModel[] items) {
        feedRepository.insert(feed);
        insertAll(items);
    }

    public void parse(String url, boolean registered, boolean subscribed, FeedToItemConverter
            .ConverterCallback callback) {
        parser.parse(url, registered, subscribed, callback);
    }

    private class GetOneTask extends AsyncTask<String,Void,ItemModel>{

        @Override
        protected ItemModel doInBackground(String... strings) {
            return dao.getOneByFeed(strings[0]);
        }
    }
}
