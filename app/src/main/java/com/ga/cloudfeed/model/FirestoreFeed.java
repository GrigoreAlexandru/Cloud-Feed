package com.ga.cloudfeed.model;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.ga.cloudfeed.Utils.toBase64;

public final class FirestoreFeed {
    @NonNull
    private String url, encodedUrl, title;
    private Date lastModified;
    private String websub;
    private Map<String, String> cloud;
    private int subscribers;


    public FirestoreFeed() {
    }

    public FirestoreFeed(FeedModel model) {
        this.url = model.url;
        this.encodedUrl = toBase64(model.url);
        this.title = model.title;
        this.lastModified = model.date;
        this.websub = model.websub;
        this.cloud = model.cloud;

    }


    public String getUrl() {
        return url;
    }

    public String getEncodedUrl() {
        return encodedUrl;
    }

    public String getTitle() {
        return title;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getWebsub() {
        return websub;
    }

    public Map<String, String> getCloud() {
        return cloud;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirestoreFeed feed = (FirestoreFeed) o;
        return Objects.equals(encodedUrl, feed.encodedUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(encodedUrl);
    }
}
