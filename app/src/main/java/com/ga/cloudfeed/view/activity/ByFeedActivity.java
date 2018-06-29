package com.ga.cloudfeed.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ga.cloudfeed.Constants;
import com.ga.cloudfeed.R;

import static com.ga.cloudfeed.view.fragment.ListFragment.BY_FEED_FRAG;

public class ByFeedActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_feed);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getListFragment(BY_FEED_FRAG, R.layout.item,
                getIntent().getStringExtra("modelLink"));
        addFragmentToActivity(manager, fragment, R.id.fl_by_feed, BY_FEED_FRAG);
        setTitle(getIntent().getStringExtra("modelTitle"));
    }
}
