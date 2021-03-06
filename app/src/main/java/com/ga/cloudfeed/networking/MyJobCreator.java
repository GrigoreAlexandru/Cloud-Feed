package com.ga.cloudfeed.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class MyJobCreator implements com.evernote.android.job.JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case PollJob.TAG:
                return new PollJob();
            case NotificationJob.TAG:
                return new NotificationJob();
            default:
                return null;
        }
    }
}
