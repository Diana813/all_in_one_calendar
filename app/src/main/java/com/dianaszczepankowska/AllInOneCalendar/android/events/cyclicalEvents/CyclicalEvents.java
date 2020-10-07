package com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.CyclicalEventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.EventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.GestureInteractionsRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static java.util.Objects.requireNonNull;

public class CyclicalEvents extends Fragment {

    private Context context;
    private CyclicalEventsListAdapter cyclicalEventsListAdapter;
    private FloatingActionButton fab;
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
        eventsListAdapter.setIndexInDB();

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
        requireNonNull(getActivity()).setTitle(getString(R.string.CyclicalEvents));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);


        fab = rootView.findViewById(R.id.fab2);
        fab.setVisibility(View.VISIBLE);
        RelativeLayout mainLayout = rootView.findViewById(R.id.mainLayout);
        mainLayout.setPadding(0, 4, 0, 0);

        RecyclerView recyclerView = rootView.findViewById(R.id.list2);
        recyclerView.setVisibility(View.VISIBLE);
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
        });
    }

    private void onFabClick() {
        fab.setOnClickListener(view1 -> showFragment(CyclicalEventsDetails.newInstance(-1, "-1", null, null, null, 0, 0, null, null)));
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireNonNull(getActivity()).getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
       /* FragmentTransaction fragmentTransaction = (requireNonNull(getActivity())).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(((ViewGroup) requireNonNull(getView()).getParent()).getId(), fragment);
        fragmentTransaction.commitNow();*/
    }

}
