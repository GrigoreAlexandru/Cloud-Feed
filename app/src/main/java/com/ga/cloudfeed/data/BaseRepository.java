package com.ga.cloudfeed.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.data.database.BaseDao;
import com.ga.cloudfeed.data.database.FeedDao;
import com.ga.cloudfeed.data.database.ItemDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class BaseRepository<T, TD extends BaseDao<T>> {
    public static final String READ_LATER = "readLater";
    public static final String READ = "read";
    public static final String SUBSCRIBED = "subscribed";
    public static final String REGISTERED = "registered";
    public static final String URL = "URL";
    final TD dao;

    public BaseRepository(TD dao) {
        this.dao = dao;
    }

    public LiveData<List<T>> getAll() {
        return dao.getAll();
    }

    public T get(String link) {
        try {
            return new GetTask<>(dao).execute(link).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(T object) {
        new InsertTask<>(dao).execute(object);
    }

    public void insertAll(T[] objects) {
        new InsertTask<>(dao).execute(objects);
    }

    public void delete(T object) {
        new DeleteTask<>(dao).execute(object);
    }

    /* ******************************* Async Tasks ***********************************************/

    static class UpdateTask extends AsyncTask<String, Void, Void> {
        private final BaseDao dao;
        private final boolean[] bol;


        UpdateTask(BaseDao dao, boolean... bol) {
            super();
            this.dao = dao;
            this.bol = bol;
        }

        @Override
        protected Void doInBackground(String... keys) {
            switch (keys[0]) {
                case READ_LATER:
                    ((ItemDao) dao).setReadLater(keys[1]);
                    break;
                case READ:
                    ((ItemDao) dao).setRead(keys[1]);
                    break;
                case SUBSCRIBED:
                    ((FeedDao) dao).setSubscribed(keys[1], bol[0]);
                    break;
                case REGISTERED:
                    ((FeedDao) dao).setRegistered(keys[1]);
                    break;
                default:
                    throw new IllegalArgumentException(keys[0] + " Is not a recognized update " +
                            "method");

            }
            return null;
        }
    }

    static class InsertTask<T> extends AsyncTask<T, Void, Void> {
        private final BaseDao<T> dao;

        InsertTask(BaseDao<T> dao) {
            super();
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(T... objects) {
            if (objects.length > 1) {
                dao.insertAll(objects);
            } else if (objects.length > 0) {
                dao.insert(objects[0]);
            } else {
                CloudFeedApplication.showToast("No items");
            }
            return null;
        }
    }

    private static class DeleteTask<T> extends AsyncTask<T, Void, Void> {
        private final BaseDao<T> dao;

        DeleteTask(BaseDao<T> dao) {
            super();
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(T... objects) {
            dao.delete(objects[0]);
            return null;
        }
    }

    private static class GetTask<T> extends AsyncTask<String, Void, T> {
        private final BaseDao<T> dao;

        GetTask(BaseDao<T> dao) {
            super();
            this.dao = dao;
        }

        @Override
        protected T doInBackground(String... links) {
            return dao.get(links[0]);
        }
    }
}
