package com.ga.cloudfeed.di;

import android.app.Application;

import com.ga.cloudfeed.networking.NotificationJob;
import com.ga.cloudfeed.networking.PollJob;
import com.ga.cloudfeed.networking.User;
import com.ga.cloudfeed.view.activity.ByFeedActivity;
import com.ga.cloudfeed.view.activity.MainActivity;
import com.ga.cloudfeed.view.activity.NotificationsActivity;
import com.ga.cloudfeed.view.activity.WebViewActivity;
import com.ga.cloudfeed.view.fragment.AddFeedDialogFragment;
import com.ga.cloudfeed.view.fragment.ListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetModule.class, ModelModule.class, MiscModule.class, RoomModule.class})
public interface AppComponent {
    void inject(AddFeedDialogFragment addFeedDialogFragment);
    void inject(ListFragment listFragment);
    void inject(WebViewActivity webViewActivity);
    void inject(ByFeedActivity byFeedActivity);
    void inject(NotificationsActivity notificationsActivity);
    void inject(MainActivity mainActivity);
    void inject(User user);
    void inject(PollJob pollJob);
    void inject(NotificationJob notificationJob);

    Application application();


}
