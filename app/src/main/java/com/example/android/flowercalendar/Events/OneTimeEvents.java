package com.example.android.flowercalendar.Events;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.flowercalendar.R;


public class OneTimeEvents extends Fragment {

    TextView eventStart;
    ImageView scheduleButton;

    public OneTimeEvents() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one_time_events, container, false);

        setHasOptionsMenu(true);

        eventStart = rootView.findViewById(R.id.eventStart);
        scheduleButton = rootView.findViewById(R.id.schedule_button);
        setScheduleButton();

        return rootView;
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.for_girls_save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                return true;

            case R.id.action_delete_all_entries:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void eventTimeSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        eventStart.setText(String.format("%02d:%02d", hour, minute));

                    }
                }, 0, 0, true);
        timePickerDialog.show();
    }

    private void setScheduleButton(){
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTimeSettingDialog();
            }
        });
    }


}
