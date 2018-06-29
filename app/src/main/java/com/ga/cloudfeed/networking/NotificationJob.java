package com.ga.cloudfeed.networking;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.data.ItemRepository;

import javax.inject.Inject;


public class NotificationJob extends Job {

    public static final String TAG = "NOTIFICATION_JOB";
    @Inject
    ItemRepository itemRepository;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        ((CloudFeedApplication) CloudFeedApplication.getContext())
                .getAppComponent()
                .inject(this);

        try {
            String url = params.getExtras().getString("url", null);
            itemRepository.parseAndInsert(url, true, true);
            return Result.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.FAILURE;

    }

    public static void runJobImmediately(String url) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putString("url", url);

        new JobRequest.Builder(NotificationJob.TAG)
                .startNow()
                .setExtras(extras)
                .build()
                .schedule();
    }
}

