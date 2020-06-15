package com.example.android.flowercalendar.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.flowercalendar.utils.AppUtils;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.events.EventsListAdapter;
import com.example.android.flowercalendar.events.EventsViewModel;
import com.example.android.flowercalendar.events.ExpandedDayView.ToDoList;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

public class OneDayPlan extends Plan {

    private Context context;
    private LocalDate pickedDay;
    private ToDoList toDoList;
    private EventsListAdapter eventsListAdapter;
    private LocalDate timeOutDate;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        toDoList = new ToDoList();
        eventsListAdapter = new EventsListAdapter(EventsListAdapter.getContext());

    }


    @Override
    public void onPause() {
        super.onPause();
        toDoList.setNumberOfEventsToPickedDate(String.valueOf(pickedDay));
        eventsListAdapter.setIndexInDB();
        addEffectivenesToDB(context, 4, firstItemDate, progress);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        question.setText(R.string.OneDayPlan);
        pickedDay = LocalDate.now();
        initData(this, adapter, planViewModel.getOneDayAimsList(), planViewModel.getOneDayAimsListIsChecked());
        initDataEvents();
        AppUtils.setConfirmButton(confirm, adapter, aimText, 4, String.valueOf(pickedDay), "-1", "", 1);
        return rootView;
    }


    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    @Override
    void initData(Fragment fragment, final BigPlanAdapter adapter, LiveData<List<BigPlanData>> listLiveData, LiveData<List<BigPlanData>> listLiveDataIsChecked) {
        super.initData(fragment, adapter, listLiveData, listLiveDataIsChecked);
        listLiveData.observe(fragment, aims -> {
            adapter.setAimsList(aims);

            if (aims.size() != 0) {

                timeOutDate = AppUtils.refactorStringIntoDate(aims.get(0).getStartDate()).plusDays(1);
                long howManyHoursLeft = howMuchTimeLeft(timeOutDate).toHours();
                timeOut.setText("Time left: " + howManyHoursLeft + " hours");

                if (timeOutDate.isBefore(LocalDate.now()) ||
                        timeOutDate.isEqual(LocalDate.now())) {
                    deleteIfTimeIsOut(4, context);

                }
                if (newId == 0) {
                    timeOut.setVisibility(View.GONE);
                }

            } else {
                timeOut.setVisibility(View.GONE);
            }

        });
    }

    private void initDataEvents() {

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        eventsViewModel.getEventsListForAims().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            eventsListAdapter.setEventsList(events);
        });

    }

}

