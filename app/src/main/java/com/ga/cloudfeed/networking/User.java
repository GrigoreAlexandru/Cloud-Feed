package com.ga.cloudfeed.networking;

import android.net.Uri;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.data.ItemRepository;
import com.ga.cloudfeed.model.FeedModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class User {
    @Inject
    ItemRepository itemRepository;
    @Inject
    FeedRepository feedRepository;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference colref = db.collection("users/");


    private static User instance;
    private String displayName = "Cloud Feed";
    private Uri photoUrl;
    private String uid;

    public User() {
    }

    private User(String uid, String displayName, Uri photoUrl) {
        this.uid = uid;
        this.displayName = displayName;
        this.photoUrl = photoUrl;

        ((CloudFeedApplication) CloudFeedApplication.getContext())
                .getAppComponent()
                .inject(this);
    }

    public static User createInstance(String uid, String displayName, Uri photoUrl) {
        instance = new User(uid, displayName, photoUrl);
        return instance;
    }

    public static User createInstance() {
        instance = new User();
        return instance;
    }

    public void syncUser() {
        if (uid != null) {
            DocumentReference userRef = getDocumentReference();
            db.runTransaction((Transaction.Function<Void>) transaction -> {
                userRef.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        List<String> feeds = (List<String>) snapshot.get("feeds");
                        if (feeds != null) {
                            insertFeeds(feeds);
                            subscribeFeeds(feeds);
                        }

                    } else {
                        userRef.set(new HashMap<String, List<String>>() {{
                            List<FeedModel> subscribed = feedRepository.getSubscribed();
                            List<String> urls = new ArrayList();
                            for (FeedModel model : subscribed) {
                                urls.add(model.url);
                            }
                            put("feeds", urls);
                        }});
                    }
                });

                return null;
            });
        }
    }

    private void subscribeFeeds(List<String> feeds) {
        Firestore instance = Firestore.getInstance();
        for (String feed : feeds) {
            instance.subscribe(feed);
        }
    }

    private void insertFeeds(List<String> urls) {
        for (String url : urls) {
            URL uri = null;
            try {
                uri = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String base = uri.getProtocol() + "://" + uri.getHost();
            FeedModel model = feedRepository.get(base);
            if (model == null) {
                itemRepository.parseAndInsert(url, true, true);
            }
        }
    }

    public DocumentReference getDocumentReference() {
        return colref.document(uid);
    }

    public static User getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public String getUid() {
        return uid;
    }
}
