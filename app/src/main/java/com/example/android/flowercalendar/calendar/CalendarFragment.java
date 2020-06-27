package com.example.android.flowercalendar.calendar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.alarm.AlarmUtils;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.PeriodDataDao;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.events.cyclicalEvents.DisplayCyclicalEventsInTheCalendar;
import com.example.android.flowercalendar.events.expandedDayView.BackgroundActivityExpandedDayView;
import com.example.android.flowercalendar.gestures.GestureInteractionsViews;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.android.flowercalendar.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;


public class CalendarFragment extends CalendarBrain {

    private static final int DAYS_COUNT = 42;
    private ImageView previousButton;
    private ImageView nextButton;
    private ImageView prevYearButton;
    private ImageView nextYearButton;
    private CardView calendarCardView;
    public String event;
    public static ArrayList<CalendarViews> calendarViewsArrayList;
    private LocalDate pickedDate;
    private int dayOfMonth;
    private LocalDate lastMondayOfCurrentMonth;
    public static String pickedDay;


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

        findViews(rootView);
        setBottomSheetsBehavior();
        setBottomLayoutShiftsAdapter();
        BottomLayoutsUtils.initShiftsData(this, shiftsAdapter);
        setHasOptionsMenu(true);
        onGridViewItemClickListener();
        swipeTheCalendar();
        deleteShiftFromPickedDate();
        loadPeriodData();
        return rootView;
    }

    private void findViews(View rootView) {

        shifts_bottom_sheet = rootView.findViewById(R.id.shiftsSettings);
        shifts_recycler_view = rootView.findViewById(R.id.shifts_list_recycler);
        shiftsDownArrow = rootView.findViewById(R.id.shiftsDownArrow);
        backgroundDrawing = rootView.findViewById(R.id.backgroundDrawing);
        gridView = rootView.findViewById(R.id.widgetGridView);
        date = rootView.findViewById(R.id.date);
        previousButton = rootView.findViewById(R.id.calendar_prev_button);
        nextButton = rootView.findViewById(R.id.calendar_next_button);
        prevYearButton = rootView.findViewById(R.id.calendar_prev_year_button);
        nextYearButton = rootView.findViewById(R.id.calendar_next_year_button);
        calendarCardView = rootView.findViewById(R.id.calendarCardView);
    }

    public void fillTheCalendar(Context context, TextView date, GridView gridView) {

        calendarViewsArrayList = new ArrayList<>();
        listOfCyclicalEvents = new ArrayList<>();
        CalendarAdapter calendarAdapter = new CalendarAdapter(context, calendarViewsArrayList);
        displayEmptyEvents();
        displayHeaderDate(date);
        setDisplayCyclicalEventsInTheCalendar();
        displayCalendarWithData();
        gridView.setAdapter(calendarAdapter);

    }


    public void displayEmptyEvents() {

        if (event == null) {
            event = "";
        }
    }


    private void displayHeaderDate(TextView date) {

        //Data wyświetlana w górnym TextView
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        String todaysDate = calendarFill.format(sdf);
        date.setText(todaysDate);

        int year = calendarFill.getYear();
        int day = calendarFill.getDayOfMonth();
        Month month = calendarFill.getMonth();

        //Zmienna do przechowywania daty, która wyświetla się w górnym TextView
        //potrzebna do prawidłowego działania buttonów i kolorowania kalendarza
        headerDate = LocalDate.of(year, month, day);
    }

    public void displayCalendarWithData() {

        calendarFill = setDateAtFirstDayOfAMonth(calendarFill);
        lastMondayOfCurrentMonth = lastMondayOfCurrentMonth(calendarFill);
        displayPeriodEveryMonth();
        calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar(calendarFill);
        //Wypełniam kalendarz
        while (calendarViewsArrayList.size() < DAYS_COUNT) {

            dayOfMonth = calendarFill.getDayOfMonth();
            displayEventsAndShiftsIfEmpty();
            event = CalendarBrain.numberOfEvents(event, context, calendarFill);

            if (periodStartDate != null) {
                displayCalendarWithPeriodData();

            } else {
                calendarViewsArrayList.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, shiftNumber, event));

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


    public void displayEventsAndShiftsIfEmpty() {

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
                    R.mipmap.period_icon_v2));

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

            calendarViewsArrayList.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, shiftNumber, event));

        }

    }


    private void onGridViewItemClickListener() {

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {

            if (shiftsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

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

        if (item == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }


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
        saveShiftToPickedDate(context, newShiftNumber, headerDate, alarmHour, alarmMinute, pickedDate);

        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
        Shift shift = shiftsDao.findByShiftName(newShiftNumber);
        getAlarmTime(shift);
        if (alarmHour != null) {
            AlarmUtils.setAlarmToPickedDay(alarmHour, alarmMinute, pickedDate, context, "Open alarm class");

        }
    }


    private void goToExpandedDayView(int position) {

        pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
        pickedDay = String.valueOf(pickedDate);
        BackgroundActivityExpandedDayView eventsList = new BackgroundActivityExpandedDayView();
        Bundle args = new Bundle();
        args.putString("pickedDay", pickedDay);
        eventsList.setArguments(args);
        getParentFragmentManager().beginTransaction().replace(R.id.flContent, eventsList).addToBackStack("tag").commit();
    }


    private void deleteShiftFromPickedDate() {

        gridView.setOnItemLongClickListener((parentView, childView, position, id) -> {

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
                getAlarmTime(shift);
                if (alarmHour != null) {
                    AlarmUtils.deleteAlarmFromAPickedDay(pickedDate, alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);
                }
            }

            return true;
        });
    }


    public void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(v -> setPreviousMonthView());
    }

    public void setPreviousYearButtonClickEvent() {
        prevYearButton.setOnClickListener(v -> setPreviousYearView());
    }

    private void setPreviousYearView() {
        CalendarUtils.flipAnimation(calendarCardView);
        //Odejmowanie miesiąca
        calendarFill = headerDate.minusYears(1);
        displayPreviousPeriod();
        fillTheCalendar(context, date, gridView);
    }


    private void setPreviousMonthView() {
        CalendarUtils.flipAnimation(calendarCardView);
        //Odejmowanie miesiąca
        calendarFill = headerDate.minusMonths(1);
        displayPreviousPeriod();
        fillTheCalendar(context, date, gridView);
    }


    public void setNextButtonClickEvent() {

        nextButton.setOnClickListener(v -> setNextMonthView());
    }

    public void setNextYearButtonClickEvent() {
        nextYearButton.setOnClickListener(v -> setNextYearView());
    }

    private void setNextYearView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.plusYears(1);
        fillTheCalendar(context, date, gridView);
    }


    private void setNextMonthView() {
        CalendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.plusMonths(1);
        fillTheCalendar(context, date, gridView);
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


    private void loadPeriodData() {

        calendarFill = LocalDate.now();

        PeriodDataDao periodDataDao = getDatabase(context).periodDataDao();
        if (periodDataDao.findLastPeriod() != null) {
            String periodStart = periodDataDao.findLastPeriod().getPeriodStartDate();
            String[] parts = periodStart.split(":");

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);

            periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            periodLenght = periodDataDao.findLastPeriod().getPeriodLength();
            cycleLenght = periodDataDao.findLastPeriod().getCycleLength();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);


            fillTheCalendar(context, date, gridView);
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();
            setPreviousYearButtonClickEvent();
            setNextYearButtonClickEvent();

        } else {

            periodStartDate = null;
            periodFinishDate = null;
            fillTheCalendar(context, date, gridView);
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();
            setPreviousYearButtonClickEvent();
            setNextYearButtonClickEvent();
        }

    }


    public void setDisplayCyclicalEventsInTheCalendar() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        List<Event> cyclicalEvents = eventsDao.findByKind(0);

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

            displayCyclicalEventsInTheCalendar.displayCyclicalEvents(event.getPickedDay(), event.getFrequency(), pickedDaysOfWeek, term, event.getEvent_name(), firstCellOfTheCalendar, lastDayOfCalendarView);

        }
    }
}

