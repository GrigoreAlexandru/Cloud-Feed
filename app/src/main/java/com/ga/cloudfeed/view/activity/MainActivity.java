package com.ga.cloudfeed.view.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evernote.android.job.JobManager;
import com.firebase.ui.auth.AuthUI;
import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.Constants;
import com.ga.cloudfeed.GlideApp;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.networking.DemoJobCreator;
import com.ga.cloudfeed.networking.Firestore;
import com.ga.cloudfeed.networking.PollJob;
import com.ga.cloudfeed.networking.User;
import com.ga.cloudfeed.view.adapter.FeedPagerAdapter;
import com.ga.cloudfeed.view.fragment.AddFeedDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static com.ga.cloudfeed.view.fragment.ListFragment.FEED_FRAG;
import static com.ga.cloudfeed.view.fragment.ListFragment.LATEST_FRAG;
import static com.ga.cloudfeed.view.fragment.ListFragment.READ_LATER_FRAG;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static {
        // init firestore
        FirebaseFirestore.getInstance()
                .setFirestoreSettings(new FirebaseFirestoreSettings
                        .Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build());
        }

    public static final String CLOUD_FEED_CHANNEL = "CLOUD_FEED_CHANNEL";
    private static final int RC_SIGN_IN = 123;
    private static final int NOTIFICATION_ACTIVITY = 321;

    public static ConnectivityManager connectivityManager;

    // This is fine.
    private static WeakReference<Activity> context;
    private static WeakReference<ProgressBar> bar;

    private MenuItem login;
    private MenuItem logout;
    private boolean logged;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private View headerView;


    public static void showProgressBar(int i) {
        if (bar != null) {
            context.get().runOnUiThread(() -> bar.get().setVisibility(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((CloudFeedApplication) getApplication())
                .getAppComponent()
                .inject(this);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cloud Feed");
        setSupportActionBar(toolbar);


        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        context = new WeakReference<>(MainActivity.this);
        bar = new WeakReference<>(findViewById(R.id.progressBar3));
        auth = FirebaseAuth.getInstance();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);


        initFab();
        initDrawer();
        initDrawerLogin(navigationView.getMenu());
        initViewPager();
        createNotificationChannel();
        initUserListener();
        initPollJob();
    }

    private void initPollJob() {
        SharedPreferences pref = getSharedPreferences("cloudFeed", MODE_PRIVATE);
        boolean pollJob = pref.getBoolean("pollJob", false);
        if (!pollJob) {
            JobManager.create(this).addJobCreator(new DemoJobCreator());
            PollJob.schedulePeriodicJob();
            pref.edit().putBoolean("pollJob", true).apply();
        }

    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                new AddFeedDialogFragment()
                        .show(getSupportFragmentManager(), "NoticeDialogFragment")
        );
    }

    private void initDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void initViewPager() {
        FeedPagerAdapter adapter = new FeedPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        addFragmentsToViewPager(viewPager, adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initDrawerLogin(Menu menu) {
        login = menu.findItem(R.id.nav_login);
        logout = menu.findItem(R.id.nav_logout);
    }

    private void updateDrawerHeader() {

        User user = User.getInstance();
        ImageView iv = headerView.findViewById(R.id.iv_drawer_header);
        TextView tv = headerView.findViewById(R.id.tv_drawer_header);
        Uri url = user.getPhotoUrl();
        tv.setText(user.getDisplayName());
        if (url != null) {
            GlideApp.with(CloudFeedApplication.getContext())
                    .load(url)
                    .fallback(R.drawable.ic_placeholder)
                    .circleCrop()
                    .into(iv);
            iv.setVisibility(View.VISIBLE);
        } else {
            iv.setVisibility(View.INVISIBLE);
        }

    }

    private void initUserListener() {
        auth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            setLogin(user != null);
            createUser(user);
        });
    }

    private User createUser(FirebaseUser fUser) {
        User user = User.createInstance();
        if (fUser != null) {
            user = User.createInstance(fUser.getUid(), fUser.getDisplayName(),
                    fUser.getPhotoUrl());
        }
        updateDrawerHeader();
        return user;
    }


    private void setLogin(boolean bol) {
        logged = bol;
        logout.setVisible(bol);
        login.setVisible(!bol);
    }

    private void addFragmentsToViewPager(ViewPager viewPager, FeedPagerAdapter adapter) {
        adapter.addFragment(getListFragment(LATEST_FRAG, R.layout.item), "LATEST");
        adapter.addFragment(getListFragment(FEED_FRAG, R.layout.item), "BY FEED");
        adapter.addFragment(getListFragment(READ_LATER_FRAG, R.layout.item), "READ " +
                "LATER");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle drawer item clicks
        int id = item.getItemId();
        switch (id) {
//            case R.id.nav_settings:
//
//                break;
            case R.id.nav_notifications:
                if (logged) {
                    startActivityForResult(new Intent(this, NotificationsActivity.class),
                            NOTIFICATION_ACTIVITY);
                } else {
                    CloudFeedApplication.showToast("You need to login to enable notifications");
                    startFirebaseUi();
                }
                break;
            case R.id.nav_login:
                startFirebaseUi();
                break;
            case R.id.nav_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnSuccessListener((Void) -> createUser(null));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startFirebaseUi() {
        AuthUI instance = AuthUI.getInstance();

        instance.signOut(this);
        //login
        startActivityForResult(
                instance.createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTIFICATION_ACTIVITY:
                Firestore.getInstance().execute();
                break;
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    createUser(auth.getCurrentUser()).syncUser();
                } else {
                    CloudFeedApplication.showToast("Signin failed");
                }
                break;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CLOUD_FEED_CHANNEL,
                    "Cloud Feed", importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
