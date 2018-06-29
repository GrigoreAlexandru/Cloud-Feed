package com.ga.cloudfeed.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.model.FeedModel;

import java.util.List;


public class FeedListViewModel extends ViewModel {
    private FeedRepository repository;

    FeedListViewModel(FeedRepository repository) {
        this.repository = repository;
    }

    FeedListViewModel(FeedModel feed) {
        this.repository = repository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }

    public LiveData<List<FeedModel>> getAll() {
        return repository.getAll();
    }

    public void insert(FeedModel feedModel) {
        repository.insert(feedModel);
    }

    public void delete(FeedModel feedModel) {
        repository.delete(feedModel);
    }
}
