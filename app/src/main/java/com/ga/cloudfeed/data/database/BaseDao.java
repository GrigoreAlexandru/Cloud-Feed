package com.ga.cloudfeed.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;

import java.util.List;


public interface BaseDao<T> {

    void insert(T object);

    void insertAll(T[] objects);

    @Delete
    void delete(T object);

    LiveData<List<T>> getAll();

    T get(String link);


}
