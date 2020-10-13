package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.EventsAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ExpandedDayEditShiftAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ShiftListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.WorkCalendarShiftListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.WorkListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.EventsViewModel;
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsEditor;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AlarmUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.GestureInteractionsRecyclerView;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.BottomLayoutsUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.confirm;
import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.shifts_bottom_sheet_expanded_day_view;
import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.shifts_recycler_view_expanded_day_view;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.displayDateInALongFormat;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.displayHeaderDateInToolbar;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

public class BackgroundActivityExpandedDayView extends Fragment {

    public static String currentDate;
    private String alarmHour;
    private String alarmMinutes;
    private boolean isAlarmOn;
    private String shiftNumber;
    private LinearLayout todoLayout;
    private LinearLayout dailyScheduleLayout;
    private LinearLayout workAddButton;
    private LinearLayout eventsAddButton;
    private Button addCalendar;
    private TextView undoLastAction;
    private WorkCalendarShiftListAdapter shiftListAdapter;
    private WorkListAdapter workListAdapter;
    private EventsAdapter eventsAdapter;
    private RecyclerView shiftsList;
    private RecyclerView worksList;
    private RecyclerView eventsList;
    private LinearLayout workLayout;
    private LinearLayout eventsLayout;
    private LinearLayout alarmLayout;
    public static BottomSheetBehavior shiftsSheetBehaviorExpandedDayView;

    public BackgroundActivityExpandedDayView() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        shiftListAdapter = new WorkCalendarShiftListAdapter(context);
        workListAdapter = new WorkListAdapter(context);
        eventsAdapter = new EventsAdapter(context);
        shiftsSheetBehaviorExpandedDayView = new BottomSheetBehavior();

    }

    @Override
    public void onPause() {
        super.onPause();
        shiftListAdapter.deleteFromDatabase();
        workListAdapter.deleteFromDatabase();
        eventsAdapter.deleteFromDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.expanded_day_view_fragment, container, false);

        //handle toolbar
        currentDate = pickedDate();
        Objects.requireNonNull(getActivity()).setTitle(displayHeaderDateInToolbar(currentDate));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);

        //handle bottom navigation view
        MainActivity.menu.findItem(R.id.events).setIcon(R.drawable.baseline_today_black_48).setChecked(true).setOnMenuItemClickListener(item -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new BackgroundActivityExpandedDayView()).addToBackStack("tag").commit();
            return true;
        });

        findViews(rootView);
        setAdapters(shiftsList, shiftListAdapter, getContext());
        setAdapters(worksList, workListAdapter, getContext());
        setAdapters(eventsList, eventsAdapter, getContext());

        setOnTodoLayoutClickListener(new ToDoList());
        setOnDailyScheduleLayoutClickListener(new DailyScheduleEvents());
        initData();


        setOnWorkAddButtinClickListener(new AddWorkFragment());
        setOnEventsAddButtonClickListener(new AddEventFragment());

        setAlarmClockSwitch(rootView);

        setBottomSheetsBehaviorShiftsExpandedDayView();
        setBottomLayoutShiftsAdapterExpandedDayView();
        MainActivity.date.setText(displayDateInALongFormat(refactorStringIntoDate(currentDate)));


        return rootView;
    }

    private void findViews(View rootView) {
        todoLayout = rootView.findViewById(R.id.todoLayout);
        dailyScheduleLayout = rootView.findViewById(R.id.dailyScheduleLayout);
        workAddButton = rootView.findViewById(R.id.workAddButton);
        eventsAddButton = rootView.findViewById(R.id.eventsAddButton);
        addCalendar = rootView.findViewById(R.id.confirmButton);
        addCalendar.setText(Objects.requireNonNull(getContext()).getString(R.string.add_calendar));
        undoLastAction = rootView.findViewById(R.id.clean);
        shiftsList = rootView.findViewById(R.id.list_of_shifts);
        eventsLayout = rootView.findViewById(R.id.eventsLayout);
        worksList = rootView.findViewById(R.id.list_of_work);
        workLayout = rootView.findViewById(R.id.workLayout);
        alarmLayout = rootView.findViewById(R.id.alarmClock);
        eventsList = rootView.findViewById(R.id.list_of_events);
    }


    private void setAdapters(RecyclerView recyclerView, WorkCalendarShiftListAdapter adapter, Context context) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void setAdapters(RecyclerView recyclerView, WorkListAdapter adapter, Context context) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void setAdapters(RecyclerView recyclerView, EventsAdapter adapter, Context context) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private String pickedDate() {
        if (getArguments() != null) {
            return getArguments().getString("pickedDay");

        } else {
            return LocalDate.now().toString();
        }

    }

    public String getPickedDate() {
        return currentDate;
    }

    @SuppressLint({"SetTextI18n", "UseSwitchCompatOrMaterialCode"})
    private void setAlarmClockSwitch(View rootView) {

        if (findShift() != null && !shiftNumber.equals("") && !findAlarmTime().equals(getString(R.string.noAlarm))) {

            alarmLayout.setVisibility(View.VISIBLE);
            Switch alarmClockSwitch = rootView.findViewById(R.id.alarmClockCheckBox);
            TextView alarmText = rootView.findViewById(R.id.alarm);

            if (isAlarmOn) {
                alarmClockSwitch.setChecked(true);
                alarmText.setText(getString(R.string.alarmOn) + " " + findAlarmTime());
            } else {
                alarmClockSwitch.setChecked(false);
                alarmText.setText(R.string.alarmClockOff);
            }
            alarmClockSwitch.setOnCheckedChangeListener((alarmClockSwitch1, isChecked) -> {

                if (isChecked) {
                    alarmText.setText(getString(R.string.alarmOn) + " " + findAlarmTime());

                    if (alarmHour != null) {
                        AlarmUtils.setAlarmToPickedDay(alarmHour, alarmMinutes, pickedAlarmDate(), getContext(), "Open alarm class", null);
                    }

                    CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                    CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(currentDate);
                    if (shiftTofind != null) {
                        shiftTofind.setAlarmOn(true);
                        calendarEventsDao.update(shiftTofind);
                    }


                } else {
                    alarmText.setText(R.string.alarmClockOff);
                    AlarmUtils.deleteAlarmFromAPickedDay(pickedAlarmDate(), alarmHour, alarmMinutes, Objects.requireNonNull(getContext()), ACTION_OPEN_ALARM_CLASS);
                    CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                    CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(currentDate);
                    shiftTofind.setAlarmOn(false);
                    calendarEventsDao.update(shiftTofind);
                }
            });
        }
    }


    private String findShift() {

        CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
        CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(currentDate);
        if (shiftTofind != null) {
            shiftNumber = shiftTofind.getShiftNumber();
            isAlarmOn = shiftTofind.isAlarmOn();
        } else {
            shiftNumber = null;
        }

        return shiftNumber;
    }

    private String findAlarmTime() {

        String alarm;
        ShiftsDao shiftsDao = getDatabase(getContext()).shiftsDao();
        Shift shift = shiftsDao.findByShiftName(findShift());
        if (shift != null) {
            alarm = shift.getAlarm();
            if (!alarm.equals("")) {
                String[] split = alarm.split(":");
                alarmHour = split[0];
                alarmMinutes = split[1];
            } else {
                CalendarEventsDao calendarEventsDao = getDatabase(getContext()).calendarEventsDao();
                CalendarEvents shiftTofind = calendarEventsDao.findBypickedDate(currentDate);
                shiftTofind.setAlarmOn(false);
                calendarEventsDao.update(shiftTofind);
                alarm = (getString(R.string.noAlarm));
            }

        } else {
            alarm = (getString(R.string.noAlarm));
        }
        return alarm;

    }

    private LocalDate pickedAlarmDate() {

        String[] parts = currentDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
    }


    private void setOnTodoLayoutClickListener(Fragment fragment) {
        todoLayout.setOnClickListener(v -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
        });
    }

    private void setOnDailyScheduleLayoutClickListener(Fragment fragment) {
        dailyScheduleLayout.setOnClickListener(v -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
        });
    }

    private void setOnWorkAddButtinClickListener(Fragment fragment) {
        workAddButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
        });
    }

    private void setOnEventsAddButtonClickListener(Fragment fragment) {
        eventsAddButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
        });
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        EventsViewModel shiftsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        shiftsViewModel.getShiftLiveData().observe(getViewLifecycleOwner(), events -> {
            shiftListAdapter.setEventsList(events);
            if (events.size() > 0) {
                workLayout.setVisibility(View.VISIBLE);
                workLayout.setBackgroundColor(Objects.requireNonNull(getContext()).getColor(R.color.whiteZilla));
                shiftsList.setVisibility(View.VISIBLE);
            }
        });

        EventsViewModel workViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        workViewModel.getWorkLiveData().observe(getViewLifecycleOwner(), workList -> {
            workListAdapter.setEventsList(workList);
            if (workList.size() > 0) {
                workLayout.setVisibility(View.VISIBLE);
                workLayout.setBackgroundColor(Objects.requireNonNull(getContext()).getColor(R.color.whiteZilla));
                worksList.setVisibility(View.VISIBLE);
            }
        });

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), events -> {
            eventsAdapter.setEventsList(events);
            if (events.size() > 0) {
                eventsLayout.setVisibility(View.VISIBLE);
                eventsLayout.setBackgroundColor(Objects.requireNonNull(getContext()).getColor(R.color.whiteZilla));
                eventsList.setVisibility(View.VISIBLE);
            }
        });


    }

    public void setBottomSheetsBehaviorShiftsExpandedDayView() {
        shiftsSheetBehaviorExpandedDayView = BottomSheetBehavior.from(shifts_bottom_sheet_expanded_day_view);
        shiftsSheetBehaviorExpandedDayView.setHideable(true);
        shiftsSheetBehaviorExpandedDayView.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void setBottomLayoutShiftsAdapterExpandedDayView() {
        ExpandedDayEditShiftAdapter shiftsAdapter = new ExpandedDayEditShiftAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        shifts_recycler_view_expanded_day_view.setLayoutManager(layoutManager);
        shifts_recycler_view_expanded_day_view.setAdapter(shiftsAdapter);
        BottomLayoutsUtils.initShiftsData(getContext(), shiftsAdapter);
    }

}
