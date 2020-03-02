package com.example.android.flowercalendar.Events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter;
import com.example.android.flowercalendar.PersonalGrowth.OneYearPlanViewModel;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrequentActivities extends Fragment {
    private Context context;
    private FrequentActivitiesListAdapter frequentActivitiesListAdapter;
    private AppUtils appUtils = new AppUtils();
    private int newId;
    private RelativeLayout empty_view;


    public FrequentActivities() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        frequentActivitiesListAdapter = new FrequentActivitiesListAdapter(context, context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        frequentActivitiesListAdapter.setIndexInDatabase();
        frequentActivitiesListAdapter.deleteFromDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        frequentActivitiesListAdapter.setIndexInDatabase();
        frequentActivitiesListAdapter.deleteFromDatabase();
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

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        empty_view = rootView.findViewById(R.id.empty_view);
        TextView eventsLabel = rootView.findViewById(R.id.eventsLabel);
        TextView emptyViewTitle = rootView.findViewById(R.id.empty_title_text);
        TextView emptyViewSubtitle = rootView.findViewById(R.id.empty_subtitle_text);
        ImageView imageView = rootView.findViewById(R.id.empty_view_image);
        emptyViewTitle.setText(R.string.addFrequentActivities);
        emptyViewSubtitle.setText(R.string.descriptionFrequentActivities);
        imageView.setImageResource(R.drawable.baseline_date_range_black_48);
        eventsLabel.setText(R.string.activitiesList);

        EditText eventText = rootView.findViewById(R.id.eventText);
        ImageButton confirm = rootView.findViewById(R.id.confirm_button);

        RecyclerView recyclerView = rootView.findViewById(R.id.list_of_events);
        recyclerView.setAdapter(frequentActivitiesListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(frequentActivitiesListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        initData(this,frequentActivitiesListAdapter);
        appUtils.setConfirmButtonFreqAct(confirm,frequentActivitiesListAdapter,eventText,1,newId);
        return rootView;
    }

    private void initData(Fragment fragment, final FrequentActivitiesListAdapter adapter) {
        FrequentActivitiesViewModel frequentActivitiesViewModel = ViewModelProviders.of(fragment).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(fragment, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                adapter.setEventsList(events);
                if (events == null) {
                    newId = 0;
                    empty_view.setVisibility(View.VISIBLE);
                } else {
                    newId = events.size();
                    empty_view.setVisibility(View.GONE);
                }
            }
        });

    }


}
