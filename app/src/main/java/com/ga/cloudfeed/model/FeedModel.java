package com.ga.cloudfeed.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.Map;

@Entity
public class FeedModel extends BaseModel {
    @NonNull
    @PrimaryKey
    public final String link;
    public final String websub;
    public final String url;
    public Boolean subscribed;
    public Boolean registered;
    public final Map<String, String> cloud;

    public FeedModel(String link, String title, String description, String image, Date date,
                     @NonNull String websub, Map<String, String> cloud, String url) {
        super(link, title, description, image, date);
        this.url = url;
        this.link = super.link1;
        this.websub = websub;
        this.cloud = cloud;
    }

    @Ignore
    public FeedModel(String link, String title, String description, String image, Date date,
                     @NonNull String websub, Map<String, String> cloud, String url, Boolean
                             subscribed, Boolean registered) {
        super(link, title, description, image, date);
        this.link = link1;
        this.websub = websub;
        this.url = url;
        this.subscribed = subscribed;
        this.registered = registered;
        this.cloud = cloud;
    }
}
