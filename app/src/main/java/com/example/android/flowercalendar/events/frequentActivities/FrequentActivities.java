package com.example.android.flowercalendar.events.frequentActivities;

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

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.events.eventsUtils.EventsListAdapter;
import com.example.android.flowercalendar.events.eventsUtils.UtilsEvents;
import com.example.android.flowercalendar.gestures.GestureInteractionsRecyclerView;

import java.util.Objects;

import androidx.annotation.NonNull;
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

        empty_view = rootView.findViewById(R.id.empty_view);
        TextView eventsLabel = rootView.findViewById(R.id.eventsLabel);
        TextView emptyViewTitle = rootView.findViewById(R.id.empty_title_text);
        TextView emptyViewSubtitle = rootView.findViewById(R.id.empty_subtitle_text);
        ImageView imageView = rootView.findViewById(R.id.empty_view_image);
        emptyViewTitle.setText(R.string.addFrequentActivities);
        emptyViewSubtitle.setText(R.string.descriptionFrequentActivities);
        imageView.setImageResource(R.drawable.baseline_date_range_black_48);
        eventsLabel.setText(R.string.activitiesList);

        EditText eventText = rootView.findViewById(R.id.editText);
        eventText.setTextColor(Color.BLACK);
        ImageButton confirm = rootView.findViewById(R.id.confirm_button);

        RecyclerView recyclerView = rootView.findViewById(R.id.list);
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(eventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        initData(this, eventsListAdapter);
        UtilsEvents.setConfirmButtonEvents(confirm, eventsListAdapter, eventText, "", null, "0", "", 2);

        return rootView;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void initData(Fragment fragment, final EventsListAdapter adapter) {
        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(fragment).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(fragment, events -> {
            adapter.setEventsList(events);
            if (events == null) {
                freqActSize = 0;
                empty_view.setVisibility(View.VISIBLE);
            } else {
                freqActSize = events.size();
                empty_view.setVisibility(View.GONE);

            }
        });
    }

    public static int getFreqActSize() {
        return freqActSize + 1;
    }

}
