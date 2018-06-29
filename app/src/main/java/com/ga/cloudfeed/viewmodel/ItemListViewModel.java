package com.ga.cloudfeed.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ga.cloudfeed.data.ItemRepository;
import com.ga.cloudfeed.model.ItemModel;

import java.util.List;


public class ItemListViewModel extends ViewModel {
    private ItemRepository repository;


    public ItemListViewModel(ItemRepository repository) {
        this.repository = repository;
    }

    public void parseAndInsert(String url) {
        repository.parseAndInsert(url, false, false);
    }

    public LiveData<List<ItemModel>> getAll() {
        return repository.getAll();
    }

    public LiveData<List<ItemModel>> getByFeed(String feedLink) {
        return repository.getByFeed(feedLink);
    }

    public LiveData<List<ItemModel>> getByDateAsc() {
        return repository.getByDateDesc();
    }


    public LiveData<List<ItemModel>> getByReadLater() {
        return repository.getByReadLater();
    }

    public void delete(ItemModel item) {
        repository.delete(item);
    }
}
