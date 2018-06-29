package com.ga.cloudfeed.networking;

import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.FirestoreFeed;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.ga.cloudfeed.Utils.toBase64;

public class Firestore {
    private static final int SUBSCRIBE = 1;
    private static final int UNSUBSCRIBE = -1;
    private final Queue<FirestoreFeed> subQueue = new ConcurrentLinkedQueue<>();
    private final Queue<FirestoreFeed> unsubQueue = new ConcurrentLinkedQueue<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference colref = db.collection("feeds/");
    private static Firestore instance;
    private final Map<FirestoreCallback, FirestoreFeed> regList = new HashMap<>();

    public static Firestore getInstance() {
        if (instance == null) {
            instance = new Firestore();
        }
        return instance;
    }


    public synchronized void subscribe(FeedModel model, FirestoreCallback callback) {

        FirestoreFeed feed = new FirestoreFeed(model);
        if (!model.registered) {
            regList.put(callback, feed);
        }
        if (unsubQueue.contains(feed)) {
            unsubQueue.remove(feed);
        } else {
            subQueue.add(feed);
        }


    }

    public synchronized void subscribe(String url) {
        subscribeTopic(toBase64(url));
    }

    public synchronized void unSubscribe(FeedModel model) {
        FirestoreFeed feed = new FirestoreFeed(model);
        if (subQueue.contains(feed)) {
            subQueue.remove(feed);
        } else {
            unsubQueue.add(feed);
        }
    }


    public synchronized void execute() {
        if (!regList.isEmpty()) {
            addFeed();
        } else {
            executeQueues();
        }
    }

    private void executeQueues() {
        if (!subQueue.isEmpty()) dispatch(subQueue, SUBSCRIBE);
        if (!unsubQueue.isEmpty()) dispatch(unsubQueue, UNSUBSCRIBE);
    }

    private synchronized void dispatch(Queue<FirestoreFeed> queue, int method) {
        FirestoreFeed feed;

        while ((feed = queue.poll()) != null) {
            String encoded = feed.getEncodedUrl();
            switch (method) {
                case SUBSCRIBE:
                    subscribeTopic(encoded);
                    break;
                case UNSUBSCRIBE:
                    unsubscribeTopic(encoded);
                    break;
            }
            DocumentReference feedRef = colref.document(encoded);


            String url = feed.getUrl();
            db.runTransaction(transaction ->
                    runTransaction(transaction, feedRef, url, method)
            ).addOnFailureListener(Throwable::printStackTrace);
        }

    }

    private Void runTransaction(Transaction transaction, DocumentReference feedRef, String url,
                                int method) {

        int subs = 0;
        try {
            subs = ((Long) transaction.get(feedRef).get("subscribers")).intValue();
        } catch (FirebaseFirestoreException e) {
            e.printStackTrace();
        }

        updateUser(transaction, url, method);


        transaction.update(feedRef, "subscribers", (subs + method));

        return null;
    }

    private synchronized void subscribeTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    private synchronized void unsubscribeTopic(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    private void updateUser(Transaction transaction, String url, int method) {
        DocumentReference userRef = User.getInstance().getDocumentReference();
        List<String> feeds = null;
        try {
            feeds = (List<String>) transaction.get(userRef).get("feeds");
        } catch (FirebaseFirestoreException e) {
            e.printStackTrace();
        }

        if (feeds == null || url == null) {
            feeds = new ArrayList<>();
        }


        if (method == SUBSCRIBE) {
            if (!feeds.contains(url)) {
                feeds.add(url);
            }
        } else {
            feeds.remove(url);
        }
        transaction.update(userRef, "feeds", feeds);
    }

    private synchronized void addFeed() {
        for (Map.Entry<FirestoreCallback, FirestoreFeed> entry : regList.entrySet()) {
            FirestoreFeed feed = entry.getValue();
            FirestoreCallback callback = entry.getKey();

            colref.document(feed.getEncodedUrl())
                    .set(feed)
                    .addOnSuccessListener(aVoid -> {
                        callback.success();
                        regList.clear();
                        executeQueues();
                    });
        }

    }


    public interface FirestoreCallback {
        void success();
    }
}
