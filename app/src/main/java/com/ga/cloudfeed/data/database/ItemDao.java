package com.ga.cloudfeed.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ga.cloudfeed.model.ItemModel;

import java.util.List;

@Dao
public interface ItemDao extends BaseDao<ItemModel> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Override
    void insertAll(ItemModel[] objects);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Override
    void insert(ItemModel object);

    @Query("SELECT * FROM ItemModel")
    @Override
    LiveData<List<ItemModel>> getAll();

    @Query("SELECT * FROM ItemModel WHERE link = :link")
    @Override
    ItemModel get(String link);

    @Query("SELECT * FROM ItemModel WHERE feedLink = :feedLink")
    LiveData<List<ItemModel>> getByFeed(String feedLink);

    @Query("SELECT * FROM ItemModel WHERE feedLink = :feedLink LIMIT 1")
    ItemModel getOneByFeed(String feedLink);

    @Query("SELECT * FROM ItemModel WHERE readLater = 1")
    LiveData<List<ItemModel>> getByReadLater();

    @Query("UPDATE ItemModel SET readLater = NOT readLater WHERE link = :link")
    void setReadLater(String link);

    @Query("UPDATE ItemModel SET read = 1 WHERE link = :link")
    void setRead(String link);

    @Query("select * from ItemModel ORDER BY date DESC")
    LiveData<List<ItemModel>> getByDateDesc();
}
