package com.ga.cloudfeed.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ga.cloudfeed.Constants;
import com.ga.cloudfeed.R;

import static com.ga.cloudfeed.view.fragment.ListFragment.FEED_FRAG;


public class NotificationsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setTitle("Notifications");
        Fragment fragment = getListFragment(FEED_FRAG, R.layout.notification_settings);
        addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.fl_notifications,
                FEED_FRAG);
    }
}
