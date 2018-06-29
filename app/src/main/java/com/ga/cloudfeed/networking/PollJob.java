package com.ga.cloudfeed.networking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.data.ItemRepository;
import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.ItemModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


public class PollJob extends Job {

    public static final String TAG = "POLL_JOB";
    @Inject
    ItemRepository itemRepository;
    @Inject
    FeedRepository feedRepository;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        ((CloudFeedApplication) CloudFeedApplication.getContext())
                .getAppComponent()
                .inject(this);


        try {
            List<FeedModel> models = feedRepository.getSubscribedLegacy();
            Log.d("mtag", "list size: " + models.size());
            for (FeedModel oldFeed : models) {
                itemRepository.parse(oldFeed.url, true, true, (newFeed, items) -> {

                    ItemModel oldItem = itemRepository.getOneByFeed(oldFeed.link);

                    if (items[0].date.compareTo(oldItem.date) > 0) {
                        Log.d("mtag", "found " + newFeed.title);
                        itemRepository.insertFeedWithItems(newFeed, items);
                        FirebaseService.sendNotification(newFeed.title, CloudFeedApplication
                                .getContext());

                    } else Log.d("mtag", "too old " + newFeed.title);
                    Log.d("mtag", items[0].date + " vs " + oldItem.date);

                });
            }
            return Result.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.FAILURE;
    }

    public static void schedulePeriodicJob() {
        new JobRequest.Builder(PollJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(30), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }
}
