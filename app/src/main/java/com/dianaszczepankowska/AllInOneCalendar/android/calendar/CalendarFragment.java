package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.CalendarProviderMethods;
import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.CalendarAdapterWithRota;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.CalendarAdapterNoRota;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AlarmUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.UserDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DisplayCyclicalEventsInTheCalendar;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView;
import com.dianaszczepankowska.AllInOneCalendar.android.gestures.GestureInteractionsViews;
import com.dianaszczepankowska.AllInOneCalendar.android.holidaysData.Holiday;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.BottomLayoutsUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.confirm;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.shiftsSheetBehaviorExpandedDayView;
import static com.google.common.collect.Lists.reverse;


public class CalendarFragment extends CalendarBrain {

    private static final int DAYS_COUNT = 42;
    private ImageView prevYearButton;
    private ImageView nextYearButton;
    private FrameLayout calendarCardView;
    public String event;
    public ArrayList<CalendarViews> calendarViewsArrayList;
    private LocalDate pickedDate;
    private int dayOfMonth;
    private LocalDate lastMondayOfCurrentMonth;
    public static String pickedDay;
    public static String accountEmail;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        displayCyclicalEventsInTheCalendar = new DisplayCyclicalEventsInTheCalendar();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_calendar, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.Calendar));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(null);
        MainActivity.menu.findItem(R.id.home).setIcon(R.drawable.baseline_home_black_48).setChecked(true);
        MainActivity.menu.findItem(R.id.events).setIcon(R.drawable.baseline_today_black_48).setOnMenuItemClickListener(item -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new BackgroundActivityExpandedDayView()).addToBackStack("tag").commit();
            return true;
        });

        findViews(rootView);

        setHasOptionsMenu(true);
        onGridViewItemClickListener();
        swipeTheCalendar();
        deleteShiftFromPickedDate();
        calendarFill = LocalDate.now();
        displayRightPeriod();


      /*  ConnectivityManager conM = (ConnectivityManager)
                Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);

        assert conM != null;
        NetworkInfo networkInfo = conM.getActiveNetworkInfo();

        LoaderManager loaderManager = getActivity().getLoaderManager();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.restartLoader(HOLIDAY_LOADER_ID, null, this);
        }
*/
        UserDataDao userDataDao = getDatabase(getContext()).userDataDao();
        UserData owner = null;
        if (userDataDao.getAllUsers() != null && !userDataDao.getAllUsers().isEmpty()) {
            owner = userDataDao.getAllUsers().get(0);
        }
        if (owner != null) {
            accountEmail = owner.getEmail();
        } else {
            getUserGoogleAccountEmail();
            if (userDataDao.getAllUsers() != null && !userDataDao.getAllUsers().isEmpty()) {
                owner = userDataDao.getAllUsers().get(0);
                accountEmail = owner.getEmail();
            }

        }

       /* try {
            GSuiteCalendarApi.buildClientServise();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
*/
        return rootView;
    }

    private void findViews(View rootView) {
        gridView = rootView.findViewById(R.id.widgetGridView);
        date = rootView.findViewById(R.id.date);
        prevYearButton = rootView.findViewById(R.id.calendar_prev_year_button);
        nextYearButton = rootView.findViewById(R.id.calendar_next_year_button);
        calendarCardView = rootView.findViewById(R.id.calendarCardView);
    }

    public void fillTheCalendar(Context context, TextView date, GridView gridView) {

        calendarViewsArrayList = new ArrayList<>();
        listOfCyclicalEvents = new ArrayList<>();

        boolean showShifts = sharedPreferences.getBoolean(getString(R.string.show_shifts), false);
        if (showShifts) {
            CalendarAdapterWithRota calendarAdapter = new CalendarAdapterWithRota(context, calendarViewsArrayList);
            displayEmptyEvents();
            displayHeaderDate(date);
            setDisplayCyclicalEventsInTheCalendar();
            displayCalendarWithData();
            gridView.setAdapter(calendarAdapter);


        } else {
            CalendarAdapterNoRota calendarAdapter = new CalendarAdapterNoRota(context, calendarViewsArrayList);
            displayEmptyEvents();
            displayHeaderDate(date);
            setDisplayCyclicalEventsInTheCalendar();
            displayCalendarWithData();
            gridView.setAdapter(calendarAdapter);
        }


    }


    public void displayEmptyEvents() {

        if (event == null) {
            event = "";
        }
    }

    private boolean displayHolidays() {
        if (holidays != null) {
            for (Holiday holiday : holidays) {
                LocalDate holidayDate = DateUtils.refactorStringIntoDate(holiday.getDate());
                if (holidayDate.getYear() <= calendarFill.getYear()) {
                    if (calendarFill.isEqual(holidayDate)) {
                        return true;
                    }
                } else {
                    break;
                }

            }
            return false;
        } else {
            return false;
        }

    }


    private void displayHeaderDate(TextView date) {

        //Data wyświetlana w górnym TextView
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        if (pickedDay != null) {
            calendarFill = DateUtils.refactorStringIntoDate(pickedDay);
        }
        String todaysDate = calendarFill.format(sdf);
        date.setText(todaysDate);

        int year = calendarFill.getYear();
        int day = calendarFill.getDayOfMonth();
        Month month = calendarFill.getMonth();

        //Zmienna do przechowywania daty, która wyświetla się w górnym TextView
        //potrzebna do prawidłowego działania buttonów i kolorowania kalendarza
        headerDate = LocalDate.of(year, month, day);
        pickedDay = null;
    }

    public void displayCalendarWithData() {

        calendarFill = setDateAtFirstDayOfAMonth(calendarFill);
        lastMondayOfCurrentMonth = lastMondayOfCurrentMonth(calendarFill);
        displayPeriodEveryMonth();
        calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
        //Wypełniam kalendarz
        while (calendarViewsArrayList.size() < DAYS_COUNT) {

            dayOfMonth = calendarFill.getDayOfMonth();
            displayEventsAndShifts();
            event = CalendarBrain.numberOfEvents(event, context, calendarFill, listOfCyclicalEvents);

            if (periodStartDate != null) {
                displayCalendarWithPeriodData();

            } else {
                calendarViewsArrayList.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, shiftNumber, event, displayHolidays()));

            }
            calendarFill = calendarFill.plusDays(1);
        }
    }


    private void displayPeriodEveryMonth() {

        //Żeby dane na temat okresu wyświetlały się kiedy rozpocznie się następny miesiąc
        if (periodStartDate != null) {
            for (int i = 0; i < 1000; i++) {
                calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
                if (periodFinishDate.isBefore(calendarFill)) {
                    periodStartDate = periodStartDate.plusDays(cycleLenght);
                    periodFinishDate = periodStartDate.plusDays(periodLenght - 1);
                }
                if (periodFinishDate.isEqual(calendarFill) || periodFinishDate.isAfter(calendarFill)) {
                    break;
                }
            }
        }

    }


    public void displayEventsAndShifts() {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents calendarEventToAdd = calendarEventsDao.findBypickedDate(String.valueOf(calendarFill));
        if (calendarEventToAdd == null) {
            shiftNumber = "";
            event = "";
        } else {
            shiftNumber = calendarEventToAdd.getShiftNumber();
            if (calendarEventToAdd.getShiftNumber() == null) {
                shiftNumber = "";
            }
            event = calendarEventToAdd.getEventsNumber();
            if (calendarEventToAdd.getEventsNumber().equals("0")
                    || calendarEventToAdd.getEventsNumber() == null) {
                event = "";
            }
        }
    }


    public void displayCalendarWithPeriodData() {

        if ((periodStartDate.isEqual(calendarFill) ||
                periodFinishDate.isEqual(calendarFill)) ||
                (periodStartDate.isBefore(calendarFill) &&
                        periodFinishDate.isAfter(calendarFill))) {

            calendarViewsArrayList.add(new CalendarViews(
                    calendarFill,
                    headerDate,
                    periodStartDate,
                    periodFinishDate,
                    dayOfMonth,
                    shiftNumber,
                    event,
                    R.mipmap.period_icon_v2,
                    displayHolidays()));

            //Kiedy w kalendarzu wypełni się komórka z ostatnim dniem okresu
            //trzeba dodać długość cyklu do daty początkowej i do daty końcowej okresu,
            //ale tylko pod warunkiem, że ten okres nie powinien wyświetlić się na
            //następnej stronie kalendarza. Jeśli musi się wyświetlić, data tego okresu powinna
            //zostać taka jak była
            if (periodFinishDate.isEqual(calendarFill) &&
                    periodFinishDate.isBefore(lastMondayOfCurrentMonth)) {

                periodStartDate = periodStartDate.plusDays(cycleLenght);
                periodFinishDate = periodFinishDate.plusDays(cycleLenght);

            }
        } else {

            calendarViewsArrayList.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, shiftNumber, event, displayHolidays()));

        }

    }


    private void onGridViewItemClickListener() {

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {

            boolean showShifts = sharedPreferences.getBoolean(getString(R.string.show_shifts), false);

            if (shiftsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && showShifts) {

                pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
                pickedDay = String.valueOf(pickedDate);
                CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
                CalendarEvents shiftToDelete = calendarEventsDao.findBypickedDate(pickedDay);
                if (shiftToDelete != null) {
                    ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
                    Shift shift = shiftsDao.findByShiftName(shiftToDelete.getShiftNumber());
                    getAlarmTime(shift);
                    if (alarmHour != null && !alarmHour.equals("")) {
                        AlarmUtils.deleteAlarmFromAPickedDay(pickedDate, alarmHour, alarmMinute, getContext(), ACTION_OPEN_ALARM_CLASS);
                        shiftToDelete.setAlarmOn(false);
                        calendarEventsDao.update(shiftToDelete);
                        alarmHour = null;
                        alarmMinute = null;
                    }
                }

                pasteShiftNumber(view, position);

            } else {

                goToExpandedDayView(position);

            }

        });
    }


    private void pasteShiftNumber(View view, int position) {

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        shiftNumber = "";

        assert clipboard != null;
        if (!(clipboard.hasPrimaryClip())) {

            view.setEnabled(false);

        } else if (!(Objects.requireNonNull(clipboard.getPrimaryClipDescription()).hasMimeType(MIMETYPE_TEXT_PLAIN))) {

            view.setEnabled(false);
        } else {

            view.setEnabled(true);
        }

        ClipData.Item item = Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0);

        view = gridView.getChildAt(position -
                gridView.getFirstVisiblePosition());

        if (view == null)
            return;

        assert item != null;
        String newShiftNumber = (String) item.getText();
        TextView shiftNumber1 = view.findViewById(R.id.shiftNumber);
        shiftNumber1.setText(newShiftNumber);
        pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
        pickedDay = String.valueOf(pickedDate);


        saveShiftToPickedDate(context, newShiftNumber, headerDate, pickedDate, accountEmail);

        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
        Shift shift = shiftsDao.findByShiftName(newShiftNumber);
        getAlarmTime(shift);

        if (alarmHour != null && !alarmHour.equals("")) {
            AlarmUtils.setAlarmToPickedDay(alarmHour, alarmMinute, pickedDate, context, ACTION_OPEN_ALARM_CLASS, null);


        }
    }


    private void goToExpandedDayView(int position) {

        pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
        pickedDay = String.valueOf(pickedDate);
        BackgroundActivityExpandedDayView eventsList = new BackgroundActivityExpandedDayView();
        Bundle args = new Bundle();
        args.putString("pickedDay", pickedDay);
        eventsList.setArguments(args);
        getParentFragmentManager().beginTransaction().replace(((ViewGroup) Objects.requireNonNull(getView()).getParent()).getId(), eventsList).addToBackStack("tag").commit();
    }


    private void deleteShiftFromPickedDate() {

        gridView.setOnItemLongClickListener((parentView, childView, position, id) -> {

            PopupMenu popupMenu = new PopupMenu(getContext(), childView);
            popupMenu.getMenuInflater().inflate(R.menu.delete_menu, popupMenu.getMenu());
            popupMenu.getMenu().findItem(R.id.action_delete_all_entries).setTitle(getString(R.string.deleteOne));

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete_all_entries) {
                    TextView shiftNumber1 = childView.findViewById(R.id.shiftNumber);
                    shiftNumber1.setText("");
                    pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();

                    pickedDay = String.valueOf(pickedDate);

                    CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
                    CalendarEvents shiftToDelete = calendarEventsDao.findBypickedDate(pickedDay);
                    if (shiftToDelete != null) {
                        calendarEventsDao.deleteBypickedDate(pickedDay);
                        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
                        Shift shift = shiftsDao.findByShiftName(shiftToDelete.getShiftNumber());
                        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
                        Event rotaToDelete = eventsDao.findByEventNameKindAndPickedDay(shiftToDelete.getShiftNumber(), pickedDay, EventKind.SHIFTS.getIntValue());
                        if(rotaToDelete != null){
                            CalendarProviderMethods.deleteEvent(context, rotaToDelete.getEventId());
                        }
                        getAlarmTime(shift);
                        if (alarmHour != null) {
                            AlarmUtils.deleteAlarmFromAPickedDay(pickedDate, alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);
                        }
                        eventsDao.deleteByPickedDateKindAndName(pickedDay, EventKind.SHIFTS.getIntValue(), shift.getShift_name());

                    }
                }
                return true;
            });
            popupMenu.show();


            return true;
        });
    }


    public void setPreviousYearButtonClickEvent() {
        prevYearButton.setOnClickListener(v -> setPreviousYearView());
    }

    private void setPreviousYearView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.minusYears(1);
        displayRightPeriod();
    }


    private void setPreviousMonthView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.minusMonths(1);
        displayRightPeriod();
    }


    public void setNextYearButtonClickEvent() {
        nextYearButton.setOnClickListener(v -> setNextYearView());
    }

    private void setNextYearView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.plusYears(1);
        displayRightPeriod();
    }


    private void setNextMonthView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.plusMonths(1);
        displayRightPeriod();
    }


    private void displayPreviousPeriod() {
        if (periodStartDate != null) {

            periodStartDate = previousPeriodStartDate();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void swipeTheCalendar() {

        gridView.setOnTouchListener(new GestureInteractionsViews(context) {

            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                setNextMonthView();
            }

            public void onSwipeLeft() {
                setPreviousMonthView();
            }

            public void onSwipeBottom() {
            }
        });
    }


    private PeriodData loadPeriodData() {

        PeriodDataDao periodDataDao = getDatabase(context).periodDataDao();
        List<PeriodData> listOfPeriods = periodDataDao.findAllPeriodData();
        listOfPeriods = reverse(listOfPeriods);
        PeriodData periodData = null;
        displayHeaderDate(date);
        LocalDate currentDate = headerDate;
        LocalDate firstDayOfTheMonth = setDateAtFirstDayOfAMonth(currentDate);
        LocalDate firstCellOfTheCalendar = findWhatDateIsInTheFirstCellOfTheCalendar(firstDayOfTheMonth);

        if (listOfPeriods != null && !listOfPeriods.isEmpty()) {
            for (PeriodData period : listOfPeriods) {

                LocalDate periodDate = displayCyclicalEventsInTheCalendar.findNewStartEventDate(DateUtils.refactorStringIntoDate(period.getPeriodStartDate()), firstCellOfTheCalendar, period.getCycleLength());
                try {
                    PeriodData nextPeriod = listOfPeriods.get(listOfPeriods.indexOf(period) + 1);
                    if (periodDate.isBefore(DateUtils.refactorStringIntoDate(nextPeriod.getPeriodStartDate()))) {
                        periodData = period;
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    periodData = periodDataDao.findLastPeriod();
                    return periodData;

                }
            }

        }

        return periodData;
    }

    private void displayRightPeriod() {

        PeriodDataDao periodDataDao = getDatabase(context).periodDataDao();

        PeriodData period = loadPeriodData();
        if (period == null) {
            periodStartDate = null;
            periodFinishDate = null;
        } else {
            String periodStart = period.getPeriodStartDate();
            String[] parts;
            if (periodStart.contains(":")) {
                parts = periodStart.split(":");
            } else {
                parts = periodStart.split("-");
            }

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);

            periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            periodLenght = periodDataDao.findLastPeriod().getPeriodLength();
            cycleLenght = periodDataDao.findLastPeriod().getCycleLength();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);
        }

        fillTheCalendar(context, date, gridView);
        setPreviousYearButtonClickEvent();
        setNextYearButtonClickEvent();
    }


    public void setDisplayCyclicalEventsInTheCalendar() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        List<Event> cyclicalEvents = eventsDao.findByKindOrderedByName(0);

        LocalDate currentDate = headerDate;
        LocalDate firstDayOfTheMonth = setDateAtFirstDayOfAMonth(currentDate);
        LocalDate firstCellOfTheCalendar = findWhatDateIsInTheFirstCellOfTheCalendar(firstDayOfTheMonth);
        LocalDate lastDayOfCalendarView = firstCellOfTheCalendar.plusDays(41);

        for (Event event : cyclicalEvents) {
            String[] parts = event.getFrequency().split("-");

            String pickedDaysOfWeek = parts[3];
            String term;
            if (event.getTerm() == null) {
                term = "on_and_on";
            } else {
                term = event.getTerm();
            }

            displayCyclicalEventsInTheCalendar.displayCyclicalEvents(event.getPickedDay(), event.getFrequency(), pickedDaysOfWeek, term, event.getEvent_name(), firstCellOfTheCalendar, lastDayOfCalendarView, listOfCyclicalEvents);

        }
    }

}

