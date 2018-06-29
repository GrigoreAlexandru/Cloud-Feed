package com.ga.cloudfeed.viewmodel;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.GlideApp;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.networking.Firestore;
import com.ga.cloudfeed.view.activity.ByFeedActivity;
import com.ga.cloudfeed.view.activity.MainActivity;


public class FeedViewModel extends BaseViewModel<FeedModel, FeedRepository> {


    public FeedViewModel(FeedRepository repository) {
        super(repository);

    }

    public void setFeed(String link) {
        this.model = repository.get(link);
    }

    public void setSubscribed(boolean bol) {
        repository.setSubscribed(model.link, bol);
    }

    public void setRegistered() {
        repository.setRegistered(model.link);
    }

    @Override
    public void itemClick(View v) {
        Intent intent = new Intent(v.getContext(), ByFeedActivity.class);
        intent.putExtra("modelLink", model.link).putExtra("modelTitle", model.title);
        v.getContext().startActivity(intent);
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .fallback(R.drawable.ic_placeholder)
                .centerCrop()
                .into(view);
    }

    public void onCheckedChanged(View view, boolean bol) {
        NetworkInfo activeNetwork = MainActivity.connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (bol != model.subscribed) {
                if (bol) {
                    Firestore.getInstance().subscribe(model, this::setRegistered);
                } else {
                    Firestore.getInstance().unSubscribe(model);
                }
                setSubscribed(bol);
            }
        } else {
            CloudFeedApplication.showToast("No Internet connection");
        }
    }
}
