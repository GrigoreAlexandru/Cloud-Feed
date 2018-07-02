package com.ga.cloudfeed;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.evernote.android.job.JobManager;
import com.ga.cloudfeed.di.AppComponent;
import com.ga.cloudfeed.di.MiscModule;
import com.ga.cloudfeed.di.DaggerAppComponent;
import com.ga.cloudfeed.di.NetModule;
import com.ga.cloudfeed.di.RoomModule;
import com.ga.cloudfeed.networking.MyJobCreator;

public final class CloudFeedApplication extends MultiDexApplication {
    private com.ga.cloudfeed.di.AppComponent AppComponent;
    private static Context context;

    public static void showToast(String data) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, data,
                Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new MyJobCreator());

        AppComponent = DaggerAppComponent
                .builder()
                .miscModule(new MiscModule(this))
                .roomModule(new RoomModule(this))
                .netModule(new NetModule())
                .build();
        CloudFeedApplication.context = getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return AppComponent;
    }

    public static Context getContext() {
        return context;
    }
}
