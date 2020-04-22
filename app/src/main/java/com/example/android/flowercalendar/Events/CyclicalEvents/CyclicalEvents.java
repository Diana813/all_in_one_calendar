package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CyclicalEvents extends Fragment {

    private Context context;
    private CyclicalEventsListAdapter cyclicalEventsListAdapter;
    private FloatingActionButton fab;
    private RelativeLayout empty_view;
    public static int newId;
    private TableLayout headerTable;
    private EventsListAdapter eventsListAdapter;

    public CyclicalEvents() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        cyclicalEventsListAdapter = new CyclicalEventsListAdapter(getContext());
        eventsListAdapter = new EventsListAdapter(getContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        cyclicalEventsListAdapter.deleteFromDatabase();

    }

    public static CyclicalEvents newInstance() {
        return new CyclicalEvents();
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
        fab.setVisibility(View.VISIBLE);
        LinearLayout editText = rootView.findViewById(R.id.editTextLinearLayout);
        editText.setVisibility(View.GONE);
        empty_view = rootView.findViewById(R.id.empty_view);
        TextView eventsLabel = rootView.findViewById(R.id.eventsLabel);
        eventsLabel.setVisibility(View.GONE);
        headerTable = rootView.findViewById(R.id.table);
        RelativeLayout mainLayout = rootView.findViewById(R.id.mainLayout);
        mainLayout.setPadding(0, 4, 0, 0);
        TextView emptyViewTitle = rootView.findViewById(R.id.empty_title_text);
        TextView emptyViewSubtitle = rootView.findViewById(R.id.empty_subtitle_text);
        ImageView imageView = rootView.findViewById(R.id.empty_view_image);
        emptyViewTitle.setText(R.string.addCyclicalEvents);
        emptyViewSubtitle.setText(R.string.descriptionCyclicalEvents);
        imageView.setImageResource(R.mipmap.cycle_event_icon);

        RecyclerView recyclerView = rootView.findViewById(R.id.list);
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

        CyclicalEventsViewModel eventsViewModel = new ViewModelProvider(this).get(CyclicalEventsViewModel.class);
        eventsViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            cyclicalEventsListAdapter.setEventsList(events);
            eventsListAdapter.setEventsList(events);
            newId = events.size();
            if (events.isEmpty()) {
                empty_view.setVisibility(View.VISIBLE);
                headerTable.setVisibility(View.GONE);
            } else {
                empty_view.setVisibility(View.GONE);
                headerTable.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onFabClick() {
        fab.setOnClickListener(view1 -> showFragment(CyclicalEventsDetails.newInstance(-1, "-1", null, null, null, 0, 0, null, null)));
    }

    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = (Objects.requireNonNull(getActivity())).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commitNow();
    }

}
