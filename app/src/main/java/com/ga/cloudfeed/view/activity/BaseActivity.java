package com.ga.cloudfeed.view.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ga.cloudfeed.view.fragment.ListFragment;

public abstract class BaseActivity extends AppCompatActivity {

    public static void addFragmentToActivity (FragmentManager fragmentManager,
                                              Fragment fragment,
                                              int frameId,
                                              String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }

    public static ListFragment getListFragment(String tag, int layoutId, String feedLink){
        ListFragment frag = ListFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putInt("layoutId", layoutId);
        bundle.putString("feedLink", feedLink);
        frag.setArguments(bundle);
        return frag;
    }

    public static ListFragment getListFragment(String tag, int layoutId){
        ListFragment frag = ListFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putInt("layoutId", layoutId);
        frag.setArguments(bundle);
        return frag;
    }
}
