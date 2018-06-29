package com.ga.cloudfeed.viewmodel;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.ga.cloudfeed.GlideApp;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.data.ItemRepository;
import com.ga.cloudfeed.model.ItemModel;
import com.ga.cloudfeed.view.HtmlTemplate;
import com.ga.cloudfeed.view.activity.MainActivity;
import com.ga.cloudfeed.view.activity.WebViewActivity;


public class ItemViewModel extends BaseViewModel<ItemModel, ItemRepository> {
    private String html;

    ItemViewModel(ItemRepository repository) {
        super(repository);
    }

    ItemViewModel(ItemModel model) {
        super(model);
    }

    @Override
    public String getDescription() {
        String desc = model.description.isEmpty() ? model.encoded : model.description;
        if (desc == null) return "";
        desc = desc.replaceAll("<.*?>\\r?\\n*", "")
                .replaceAll("^\\s*", "");
        return desc.length() > 70 ? desc.substring(0, 70) : desc;
    }

    public String getHtml() {
        if (html == null) {
            html = new HtmlTemplate().getHtml(model, getDate());
        }
        return html;
    }

    public void setModelById(String link) {
        super.model = repository.get(link);
    }

    public void setReadLater() {
        repository.setReadLater(model.link);
    }

    public void setRead() {
        repository.setRead(model.link);
    }

    public boolean getRead() {
        return model.read;
    }

    public boolean getReadLater() {
        return model.readLater;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        GlideApp.with(view.getContext())
                .load(imageUrl)
                .fallback(R.drawable.ic_placeholder)
                .centerCrop()
                .into(view);
    }

    @Override
    public void itemClick(View v) {
        setRead();
        MainActivity.showProgressBar(View.VISIBLE);
        v.getContext().startActivity(new Intent(v.getContext(), WebViewActivity.class)
                .putExtra("modelLink", model.link));
    }

}
