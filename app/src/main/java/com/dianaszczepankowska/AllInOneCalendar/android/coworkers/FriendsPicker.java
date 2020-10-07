package com.dianaszczepankowska.AllInOneCalendar.android.coworkers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class FriendsPicker extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.friends_picker, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
