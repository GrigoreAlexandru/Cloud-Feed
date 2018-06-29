package com.ga.cloudfeed.view.adapter;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.ga.cloudfeed.model.FeedModel;
import com.ga.cloudfeed.model.ItemModel;
import com.ga.cloudfeed.viewmodel.FeedViewModel;
import com.ga.cloudfeed.viewmodel.ItemViewModel;

import java.util.List;

import javax.inject.Inject;


public class GenericAdapter<BaseModel> extends RecyclerView.Adapter<GenericAdapter.BindingHolder> {
    private List<BaseModel> items;
    ViewModelProvider.Factory factory;
    private int layoutId;

    @Inject
    public GenericAdapter(ViewModelProvider.Factory factory) {
        this.factory = factory;
    }

    public void setup(List<BaseModel> items, int layout) {
        this.items = items;
        this.layoutId = layout;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutId,
                parent,
                false);
        return new BindingHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ViewDataBinding itemBinding = holder.binding;

        BaseModel currentItem = items.get(position);

        if (currentItem instanceof ItemModel) {
            ItemViewModel model = factory.create(ItemViewModel.class);
            model.setModel((ItemModel) currentItem);
            itemBinding.setVariable(BR.viewModel, model);
        } else {
            FeedViewModel model = factory.create(FeedViewModel.class);
            model.setModel((FeedModel) currentItem);
            itemBinding.setVariable(BR.viewModel, model);
        }
        itemBinding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BindingHolder<BaseModel> extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;


        BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
