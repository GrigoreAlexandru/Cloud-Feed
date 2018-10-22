package com.ga.cloudfeed.view.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.view.activity.MainActivity;
import com.ga.cloudfeed.view.adapter.GenericAdapter;
import com.ga.cloudfeed.viewmodel.FeedListViewModel;
import com.ga.cloudfeed.viewmodel.ItemListViewModel;

import java.util.List;

import javax.inject.Inject;


public class ListFragment extends android.support.v4.app.Fragment {
    public static final String FEED_LINK = "feedLink";
    public static final String LAYOUT_ID = "layoutId";
    // Fragment showing a recyclerview of ItemModel
    public static final String LATEST_FRAG = "LATEST_FRAG";
    // Fragment showing a recyclerview of FeedModel
    public static final String FEED_FRAG = "FEED_FRAG";
    // Fragment showing a recyclerview of ItemModel filtered by parent feed
    public static final String BY_FEED_FRAG = "BY_FEED_FRAG";
    // Fragment showing a recyclerview of ItemModel filtered by readLater field
    public static final String READ_LATER_FRAG = "READ_LATER_FRAG";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    GenericAdapter adapter;

    private RecyclerView recyclerView;
    private int layoutId;
    private LinearLayoutManager layoutManager;
    private FragmentActivity activity;

    public ListFragment() {
        //empty constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        ((CloudFeedApplication) activity.getApplication())
                .getAppComponent()
                .inject(this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(activity);
        View layout = inflater.inflate(R.layout.fragment_main_list, container, false);
        recyclerView = layout.findViewById(R.id.rc_main_list_fragment);

        Bundle arguments = getArguments();

        String tag;
        String feedLink = null;
        if ((tag = arguments.getString("tag")) == null) throw new IllegalArgumentException();
        if (arguments.containsKey(FEED_LINK)) feedLink = arguments.getString(FEED_LINK);
        if (arguments.containsKey(LAYOUT_ID)) layoutId = arguments.getInt(LAYOUT_ID);

        // The type of the fragment is chosen by its corresponding tag
        switch (tag) {
            // 1st fragment from viewpager
            case LATEST_FRAG:
                ItemListViewModel itemListViewModel = ViewModelProviders
                        .of(activity, viewModelFactory)
                        .get(ItemListViewModel.class);
                itemListViewModel.getByDateAsc().observe(activity, this::setItems);
                break;
            // 2nd fragment from viewpager
            case FEED_FRAG:
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                        (recyclerView.getContext(),
                        layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);

                FeedListViewModel feedListViewModel = ViewModelProviders
                        .of(activity, viewModelFactory)
                        .get(FeedListViewModel.class);
                feedListViewModel.getAll().observe(activity, this::setItems);
                break;
            // 3rd fragment from viewpager
            case READ_LATER_FRAG:
                ItemListViewModel readLaterListViewModel = ViewModelProviders
                        .of(activity, viewModelFactory)
                        .get(ItemListViewModel.class);
                readLaterListViewModel.getByReadLater().observe(this, this::setItems);
                break;
            // fragment from FEED_FRAG
            case BY_FEED_FRAG:
                ItemListViewModel byFeedListViewModel = ViewModelProviders
                        .of(activity, viewModelFactory)
                        .get(ItemListViewModel.class);
                byFeedListViewModel.getByFeed(feedLink).observe(this, this::setItems);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return layout;
    }

    private void setItems(List items) {
        recyclerView.setLayoutManager(layoutManager);
        adapter.setup(items, layoutId);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.showProgressBar(View.GONE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
