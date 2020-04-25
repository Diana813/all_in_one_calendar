package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.Events.EventsViewModel;
import com.example.android.flowercalendar.Events.FrequentActivities.FrequentActivitiesViewModel;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoList extends Fragment {

    private static int eventsListSize;
    private EventsListAdapter toDoListAdapter;
    private FrequentActivitiesDrawerListAdapter frequentActivitiesDrawerListAdapter;
    private Context context;
    private String pickedDay;
    private DrawerLayout mDrawer;
    private int layout;
    private RecyclerView freqActDrawList;
    private RecyclerView toDoListRecyclerView;
    private AppUtils appUtils;
    private ImageButton confirm;
    private EditText editText;
    private TextView eventsLabel;
    private Button freqActButton;
    private NavigationView navigationView;


    public ToDoList() {
        // Required empty public constructor
    }

    public static ToDoList newInstance() {
        return new ToDoList();
    }

    public void setContent(int layout) {
        this.layout = layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        toDoListAdapter = new EventsListAdapter(context);
        frequentActivitiesDrawerListAdapter = new FrequentActivitiesDrawerListAdapter(context, context);
        appUtils = new AppUtils();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        toDoListAdapter.setIndexInDB();
        toDoListAdapter.deleteFromDatabase(null);
        setNumberOfEventsToPickedDate(pickedDay);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout, container, false);

        findViews(rootView);
        eventsLabel.setVisibility(View.GONE);
        editText.setTextColor(Color.BLACK);
        freqActButton.setVisibility(View.VISIBLE);
        setFreqActButton();
        setAdapters();
        setHasOptionsMenu(true);
        pickedDay = findWhatDateItIs();
        initData();
        addFreqActivList();
        appUtils.setConfirmButtonEvents(confirm, toDoListAdapter, editText, pickedDay, null, "0");
        return rootView;
    }

    private void findViews(View rootView) {
        freqActDrawList = rootView.findViewById(R.id.widgetGridView);
        toDoListRecyclerView = rootView.findViewById(R.id.list);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        confirm = rootView.findViewById(R.id.confirm_button);
        editText = rootView.findViewById(R.id.editText);
        eventsLabel = rootView.findViewById(R.id.eventsLabel);
        freqActButton = rootView.findViewById(R.id.freqActButton);
        mDrawer = rootView.findViewById(R.id.activity_expanded_day_view);
        navigationView = rootView.findViewById(R.id.nvView);
    }

    private void setAdapters() {
        freqActDrawList.setAdapter(frequentActivitiesDrawerListAdapter);
        freqActDrawList.setLayoutManager(new LinearLayoutManager(context));
        toDoListRecyclerView.setAdapter(toDoListAdapter);
        toDoListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(toDoListAdapter));
        itemTouchHelper.attachToRecyclerView(toDoListRecyclerView);
        mDrawer.setScrimColor(Color.TRANSPARENT);

    }

    void saveEvent(String newEvent, String pickedDate) {
        AppUtils appUtils = new AppUtils();
        appUtils.saveDataEvents(null, pickedDate, newEvent, "-1");
    }

    private String findWhatDateItIs() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BackgroundActivityExpandedDayView) {
            pickedDay = ((BackgroundActivityExpandedDayView) parentFragment).getPickedDate();
        }
        return pickedDay;
    }


    public void setNumberOfEventsToPickedDate(String pickedDay) {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.saveEventsNumberToPickedDate(pickedDay);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> removeData());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void removeData() {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        eventsDao.deleteByPickedDate(pickedDay);
    }

    private void setFreqActButton() {

        freqActButton.setOnClickListener(v -> {
            mDrawer.openDrawer(navigationView);
            toDoListAdapter.deleteFromDatabase(null);
        });
    }

    private void initData() {

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            toDoListAdapter.setEventsList(events);
            eventsListSize = events.size();

        });
    }


    private void addFreqActivList() {

        FrequentActivitiesViewModel frequentActivitiesViewModel = new ViewModelProvider(this).get(FrequentActivitiesViewModel.class);
        frequentActivitiesViewModel.getEventsList().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            frequentActivitiesDrawerListAdapter.setEventsList(events);
        });
    }

    public static int positionOfTheNextEventOnTheList() {
        return eventsListSize + 1;
    }
}
