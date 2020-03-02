package com.example.android.flowercalendar.Events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CyclicalEvents extends Fragment {
    private Context context;
    private CyclicalEventsListAdapter cyclicalEventsListAdapter;
    private FloatingActionButton fab;
    private RelativeLayout empty_view;
    public static int newId;


    public CyclicalEvents() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        cyclicalEventsListAdapter = new CyclicalEventsListAdapter(context, context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        cyclicalEventsListAdapter.setIndexInDatabase();
        cyclicalEventsListAdapter.deleteFromDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cyclicalEventsListAdapter.setIndexInDatabase();
        cyclicalEventsListAdapter.deleteFromDatabase();
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
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.CyclicalEvents));

        fab = rootView.findViewById(R.id.fab);
        empty_view = rootView.findViewById(R.id.empty_view);
        TextView eventsLabel = rootView.findViewById(R.id.eventsLabel);
        TextView emptyViewTitle = rootView.findViewById(R.id.empty_title_text);
        TextView emptyViewSubtitle = rootView.findViewById(R.id.empty_subtitle_text);
        ImageView imageView = rootView.findViewById(R.id.empty_view_image);
        eventsLabel.setText(R.string.cyclicalEventsList);
        emptyViewTitle.setText(R.string.addCyclicalEvents);
        emptyViewSubtitle.setText(R.string.descriptionCyclicalEvents);
        imageView.setImageResource(R.mipmap.cycle_event_icon);

        LinearLayout editTextPlan = rootView.findViewById(R.id.editTextPlans);
        editTextPlan.setVisibility(View.GONE);

        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(cyclicalEventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(cyclicalEventsListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        onFabClick();
        initData();
        return rootView;
    }

    private void initData() {

        CyclicalEventsViewModel eventsViewModel = ViewModelProviders.of(this).get(CyclicalEventsViewModel.class);
        eventsViewModel.getEventsList().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                assert events != null;
                cyclicalEventsListAdapter.setEventsList(events);
                newId = events.size();
                if (events.isEmpty()) {
                    empty_view.setVisibility(View.VISIBLE);
                } else {
                    empty_view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void onFabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeEvents oneTimeEvents = new OneTimeEvents();
                Bundle args = new Bundle();
                args.putString("pickedDay", null);
                oneTimeEvents.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.flContent, oneTimeEvents).commit();
            }
        });
    }



}
