package com.ga.cloudfeed.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ga.cloudfeed.model.FeedModel;

import java.util.List;

@Dao
public interface FeedDao extends BaseDao<FeedModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Override
    void insert(FeedModel object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Override
    void insertAll(FeedModel[] objects);

    @Query("SELECT * FROM FeedModel ORDER BY date ASC")
    @Override
    LiveData<List<FeedModel>> getAll();

    @Query("SELECT * FROM FeedModel WHERE link = :link")
    @Override
    FeedModel get(String link);

    @Query("SELECT * FROM FeedModel WHERE subscribed = 1")
    List<FeedModel> getSubscribed();

    @Query("SELECT * FROM FeedModel WHERE subscribed = 1 AND websub IS NULL AND cloud = 'null'")
    List<FeedModel> getSubscribedLegacy();

    @Query("UPDATE FeedModel SET subscribed = :bol WHERE link = :link")
    void setSubscribed(String link, boolean bol);

    @Query("UPDATE FeedModel SET registered = 1 WHERE link = :link")
    void setRegistered(String link);

}
