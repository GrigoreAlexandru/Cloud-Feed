package com.ga.cloudfeed.data;

import android.os.AsyncTask;

import com.ga.cloudfeed.data.database.FeedDao;
import com.ga.cloudfeed.model.FeedModel;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class FeedRepository extends BaseRepository<FeedModel, FeedDao> {
    public FeedRepository(FeedDao dao) {
        super(dao);
    }

    public List<FeedModel> getSubscribedLegacy() {
        try {
            return new getSubscribedLegacyTask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FeedModel> getSubscribed() {
        try {
            return new getSubscribedTask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSubscribed(String link, boolean bol) {
        new UpdateTask(dao, bol).execute(SUBSCRIBED, link);
    }

    public void setRegistered(String link) {
        new UpdateTask(dao).execute(REGISTERED, link);
    }


    private class getSubscribedLegacyTask extends AsyncTask<Void, Void, List<FeedModel>> {

        @Override
        protected List<FeedModel> doInBackground(Void... voids) {
            return dao.getSubscribedLegacy();
        }
    }

    private class getSubscribedTask extends AsyncTask<Void, Void, List<FeedModel>> {

        @Override
        protected List<FeedModel> doInBackground(Void... voids) {
            return dao.getSubscribed();
        }
    }
}
