package com.ga.cloudfeed.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = FeedModel.class,
        parentColumns = "link",
        childColumns = "feedLink",
        onDelete = CASCADE))
public class ItemModel extends BaseModel {
    @NonNull
    @PrimaryKey
    public final String link;
    public final String  feedLink, feedTitle, encoded, author;
    public final ArrayList<String> categories;

    public Boolean readLater = false;
    public Boolean read = false;

    public ItemModel(String link, String title, String description, String image, Date date,
                      String feedLink, String feedTitle, String encoded,
                     String author, ArrayList<String> categories) {
        super(link, title, description, image, date);
        this.link = super.link1;
        this.feedLink = feedLink;
        this.feedTitle = feedTitle;
        this.encoded = encoded;
        this.author = author;
        this.categories = categories;
    }
}
