package com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.EventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.GestureInteractionsRecyclerView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrequentActivities extends Fragment {

    private Context context;
    private EventsListAdapter eventsListAdapter;
    private static int freqActSize;
    private RelativeLayout empty_view;


    public FrequentActivities() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        eventsListAdapter = new EventsListAdapter(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        eventsListAdapter.setIndexInDB();
        eventsListAdapter.deleteFromDatabase(null);
    }


    public static FrequentActivities newInstance() {
        return new FrequentActivities();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_events, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.FrequentActivities));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);


        ImageButton confirm = rootView.findViewById(R.id.confirm_button);

        RecyclerView recyclerView = rootView.findViewById(R.id.list2);
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
       /* ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
        initData(this, eventsListAdapter);
        //UtilsEvents.setConfirmButtonEvents(context, confirm, eventsListAdapter, eventText, "", null, "0", "", 2);

        return rootView;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void initData(Fragment fragment, final EventsListAdapter adapter) {
        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(fragment).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(fragment, events -> {
            adapter.setEventsList(events);
            if (events == null) {
                freqActSize = 0;
            } else {
                freqActSize = events.size();

            }
        });
    }

    public static int getFreqActSize() {
        return freqActSize + 1;
    }

}
