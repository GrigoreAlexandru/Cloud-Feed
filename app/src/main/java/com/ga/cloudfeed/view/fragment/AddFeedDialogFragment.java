package com.ga.cloudfeed.view.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ga.cloudfeed.CloudFeedApplication;
import com.ga.cloudfeed.R;
import com.ga.cloudfeed.viewmodel.ItemListViewModel;

import javax.inject.Inject;


public class AddFeedDialogFragment extends DialogFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ItemListViewModel itemListViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CloudFeedApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_dialog, null);
        final EditText editText = view.findViewById(R.id.et_dialog);


        builder.setView(view)
                .setPositiveButton("Done", (dialog, id) -> {
                    String inputTxt;

                    if (!(inputTxt = editText.getText().toString()).isEmpty()) {
                        itemListViewModel.parseAndInsert(inputTxt);
                    }
                })

                .setNegativeButton("Cancel", (dialog, id) ->
                        AddFeedDialogFragment.this.getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itemListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ItemListViewModel.class);
    }
}
