package com.ga.cloudfeed.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.ga.cloudfeed.data.BaseRepository;
import com.ga.cloudfeed.model.BaseModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class BaseViewModel<T extends BaseModel, R extends BaseRepository> extends ViewModel {
    protected T model;
    protected R repository;

    BaseViewModel(R repository) {
        this.repository = repository;
    }

    BaseViewModel(T model) {
        this.model = model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }

    public String getTitle() {
        String title = model.title;
        return title.length() > 50 ? title.substring(0, 50) : title;
    }

    public String getLink() {
        return model.link1;
    }

    public String getDescription() {
        return model.description;
    }

    public String getImage() {
        return model.image;
    }

    public String getDate() {
        DateFormat df = new SimpleDateFormat("MM/dd");
        String date = "date is unavailable";
        if (model.date != null) {
            date = df.format(model.date);
        }
        return date;
    }

    public void delete(View v) {
        repository.delete(model);
    }

    public void itemClick(View v) {
    }
}
