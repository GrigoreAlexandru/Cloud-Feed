package com.ga.cloudfeed.model;


import java.util.List;

public class FirestoreUser {
    private String title = "feeds";
    private String[] feeds;

    public FirestoreUser(String title, String[] feeds) {
        this.title = title;
        this.feeds = feeds;
    }

    public FirestoreUser(List<FeedModel> models) {
        for (int i = 0; i < models.size(); i++) {
            feeds[i] = models.get(i).url;
        }
    }

    public String getTitle() {
        return title;
    }

    public String[] getFeeds() {
        return feeds;
    }


}
