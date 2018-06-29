package com.ga.cloudfeed.data.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.ga.cloudfeed.data.Converters;
import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.ItemModel;

@android.arch.persistence.room.Database(entities = {ItemModel.class, FeedModel.class},
        version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FeedDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();

    public abstract FeedDao feedDao();
}
