package com.ga.cloudfeed.viewmodel;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ga.cloudfeed.data.FeedRepository;
import com.ga.cloudfeed.data.ItemRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final ItemRepository itemRepository;
    private final FeedRepository feedRepository;


    @Inject
    public CustomViewModelFactory(ItemRepository itemRepository, FeedRepository feedRepository) {
        this.itemRepository = itemRepository;
        this.feedRepository = feedRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemListViewModel.class))
            return (T) new ItemListViewModel(itemRepository);
        else if (modelClass.isAssignableFrom(ItemViewModel.class))
            return (T) new ItemViewModel(itemRepository);
        else if (modelClass.isAssignableFrom(FeedListViewModel.class))
            return (T) new FeedListViewModel(feedRepository);
        else if (modelClass.isAssignableFrom(FeedViewModel.class))
            return (T) new FeedViewModel(feedRepository);
        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
