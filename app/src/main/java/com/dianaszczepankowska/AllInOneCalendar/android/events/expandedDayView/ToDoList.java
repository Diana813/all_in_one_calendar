package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.CalendarProviderMethods;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.GestureInteractionsRecyclerView;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.PlanViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents.addNotification;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.savePlansData;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.displayHeaderDateInToolbar;

public class ToDoList extends ExpandedDayEvents {


    private PlansRecyclerViewAdapter toDoListAdapter;
    /* private FrequentActivitiesDrawerListAdapter frequentActivitiesDrawerListAdapter;
     private RecyclerView freqActDrawList;*/
    private RecyclerView toDoListRecyclerView;
    private TextView clean;
    private Button confirmButton;
    private int progress;
    private TextView effectiveness;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toDoListAdapter = new PlansRecyclerViewAdapter(context);
        //frequentActivitiesDrawerListAdapter = new FrequentActivitiesDrawerListAdapter(context);
    }


    @Override
    public void onPause() {
        super.onPause();
        toDoListAdapter.setAimIndexInDB();
        toDoListAdapter.deleteFromDatabase();
        CalendarUtils.saveEventsNumberToPickedDate(currentDate, context);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.personal_growth_plans_layout, container, false);

        //handle toolbar and bottom navigationView
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(displayHeaderDateInToolbar(currentDate));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(R.drawable.baseline_chevron_left_black_24);

        findViews(rootView);
        initData();
        setAdapters();
        setHasOptionsMenu(true);
        CalendarProviderMethods.getDataFromEventTable(context);
        //addFreqActivList();
        //displayCyclicalEvents();
        setConfirmButtonListener(getContext());
        return rootView;
    }

    private void findViews(View rootView) {
        //freqActDrawList = rootView.findViewById(R.id.widgetGridView);
        toDoListRecyclerView = rootView.findViewById(R.id.list2);
        toDoListRecyclerView.setVisibility(View.VISIBLE);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        navigationView = rootView.findViewById(R.id.nvView);
        TextView todoTitle = rootView.findViewById(R.id.title);
        todoTitle.setVisibility(View.VISIBLE);
        clean = rootView.findViewById(R.id.clean);
        clean.setVisibility(View.VISIBLE);
        FrameLayout confirmLayout = rootView.findViewById(R.id.confirmLayout);
        confirmLayout.setVisibility(View.VISIBLE);
        confirmButton = rootView.findViewById(R.id.confirmButton);
        confirmButton.setText(context.getString(R.string.add));
        RelativeLayout timeOut = rootView.findViewById(R.id.time);
        timeOut.setVisibility(View.GONE);
        effectiveness = rootView.findViewById(R.id.effectivenessResult);
    }

    private void setAdapters() {
        /*freqActDrawList.setAdapter(frequentActivitiesDrawerListAdapter);
        freqActDrawList.setLayoutManager(new LinearLayoutManager(context));*/
        toDoListRecyclerView.setAdapter(toDoListAdapter);
        toDoListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(toDoListAdapter));
        itemTouchHelper.attachToRecyclerView(toDoListRecyclerView);
        //mDrawer.setScrimColor(Color.TRANSPARENT);

    }

    //method to save plan from frequent activities list
    public void saveEvent(String newEvent, String pickedDate) {
       savePlansData(toDoListAdapter, newEvent, 5, pickedDate);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog(1);
            initData();
            return true;
        } else if (item.getItemId() == R.id.action_open_drawer) {
            mDrawer.openDrawer(navigationView);
            toDoListAdapter.deleteFromDatabase();
            return true;
        } else if (item.getItemId() == R.id.action_add_notification) {
            addNotification(getView(), context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //data from personal_growth table
    @SuppressLint("SetTextI18n")
    private void initData() {

        PlanViewModel todolistViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        todolistViewModel.getTodoListLiveData().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            toDoListAdapter.setAimsList(events);
            eventsListSize = events.size();
            //toolbar title
            Objects.requireNonNull(getActivity()).setTitle(context.getString(R.string.tasks_to_be_done) + " " + "(" + eventsListSize + ")");

            PlanViewModel todolistViewModelIsChecked = new ViewModelProvider(this).get(PlanViewModel.class);
            todolistViewModelIsChecked.getTodoListLiveDataIsChecked().observe(getViewLifecycleOwner(), todoListIsChecked -> {

                int newId = events.size();
                if (todoListIsChecked.size() != 0 && newId != 0) {
                    progress = todoListIsChecked.size() * 100 / newId;
                    effectiveness.setVisibility(View.VISIBLE);
                    effectiveness.setText(progress + "%");
                } else if (events.size() == 0) {
                    progress = -1;
                } else {
                    progress = 0;
                    effectiveness.setText(progress + "%");
                }
            });

        });
    }

   /* private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }*/

    private void setConfirmButtonListener(Context context) {
        confirmButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.task));

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            builder.setView(input);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> savePlansData(toDoListAdapter, input, 5, currentDate));
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }


}
