package com.example.android.flowercalendar.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.ExpandedDayView.BackgroundActivityExpandedDayView;
import com.example.android.flowercalendar.GestureInteractionsViews;
import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.PeriodDataDao;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;


public class CalendarFragment extends Fragment {

    private GridView gridView;
    private static final int DAYS_COUNT = 42;
    private ImageView previousButton;
    private ImageView nextButton;
    private TextView date;
    private int cycleLenght;
    private int periodLenght;
    private LocalDate periodStartDate;
    private LocalDate periodFinishDate;
    private LocalDate calendarFill;
    private CardView calendarCardView;
    private LocalDate headerDate;
    private BottomSheetBehavior sheetBehavior;
    private BottomSheetBehavior shiftsSheetBehavior;
    private RelativeLayout red;
    private RelativeLayout blue;
    private RelativeLayout yellow;
    private RelativeLayout green;
    private RelativeLayout violet;
    private RelativeLayout grey;
    private ImageView colorSettingsDownArrow;
    private ImageView shiftsDownArrow;
    private BottomLayoutShiftsAdapter shiftsAdapter;
    private Context context;
    private String shiftNumber;
    private ColorsDao colorsDao;
    private Colors colorToUpdate;
    private String event;
    private ArrayList<CalendarViews> calendarViewsArrayList;
    public static String pickedDay;
    private LocalDate pickedDate;
    private String newShiftNumber;
    private LinearLayout backgroundDrawing;
    private CalendarUtils calendarUtils = new CalendarUtils();
    private int dayOfMonth;
    private LocalDate lastMondayOfCurrentMonth;
    private BottomLayoutsUtils bottomLayoutsUtils = new BottomLayoutsUtils();
    private CardView bottom_sheet_colors;
    private LinearLayout shifts_bottom_sheet;
    private RecyclerView shifts_recycler_view;
    private String alarmHour;
    private String alarmMinute;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        shiftsAdapter = new BottomLayoutShiftsAdapter(context, context);
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
        bottomLayoutsUtils.initShiftsData(this, shiftsAdapter);
        setHasOptionsMenu(true);
        onGridViewItemClickListener();
        swipeTheCalendar();
        deleteShiftFromPickedDate();
        loadPeriodData();
        return rootView;
    }


    private void findViews(View rootView) {

        bottom_sheet_colors = rootView.findViewById(R.id.colorSettings);
        shifts_bottom_sheet = rootView.findViewById(R.id.shiftsSettings);
        shifts_recycler_view = rootView.findViewById(R.id.shifts_list_recycler);
        red = rootView.findViewById(R.id.red);
        yellow = rootView.findViewById(R.id.yellow);
        green = rootView.findViewById(R.id.green);
        blue = rootView.findViewById(R.id.blue);
        violet = rootView.findViewById(R.id.violet);
        grey = rootView.findViewById(R.id.grey);
        colorSettingsDownArrow = rootView.findViewById(R.id.colorSettingsDownArrow);
        shiftsDownArrow = rootView.findViewById(R.id.shiftsDownArrow);
        backgroundDrawing = rootView.findViewById(R.id.backgroundDrawing);
        gridView = rootView.findViewById(R.id.gridView);
        date = rootView.findViewById(R.id.date);
        previousButton = rootView.findViewById(R.id.calendar_prev_button);
        nextButton = rootView.findViewById(R.id.calendar_next_button);
        calendarCardView = rootView.findViewById(R.id.calendarCardView);
    }


    private void setBottomSheetsBehavior() {

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_colors);
        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        shiftsSheetBehavior = BottomSheetBehavior.from(shifts_bottom_sheet);
        shiftsSheetBehavior.setHideable(true);
        shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    private void setBottomLayoutShiftsAdapter() {

        shiftsAdapter = new BottomLayoutShiftsAdapter(context, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        shifts_recycler_view.setLayoutManager(layoutManager);
        shifts_recycler_view.setAdapter(shiftsAdapter);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.calendar_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logOut) {
            launchLoginActivity();
            return true;
        }
        if (item.getItemId() == R.id.settingsColor) {
            bottomLayoutsUtils.checkColorLayoutState(sheetBehavior);
            bottomLayoutsUtils.bottomSheetListener(sheetBehavior, colorSettingsDownArrow, red, yellow, green, blue, violet, grey, context, this);
            return true;
        }
        if (item.getItemId() == R.id.settingShifts) {
            bottomLayoutsUtils.checkShiftsLayoutState(shiftsSheetBehavior);
            bottomLayoutsUtils.shiftsBottomSheetListener(shiftsSheetBehavior, shiftsDownArrow);
            return true;
        }

        if (item.getItemId() == R.id.deleteShifts) {
            calendarUtils.showDeleteConfirmationDialog(context, headerDate, date, backgroundDrawing, gridView);
            return true;
        }

        if (item.getItemId() == R.id.deleteColors) {
            ColorsDao colorsDao = getDatabase(context).colorsDao();
            colorsDao.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    void fillTheCalendar(Context context, TextView date, LinearLayout backgroundDrawing, GridView gridView) {

        calendarViewsArrayList = new ArrayList<>();
        CalendarAdapter calendarAdapter = new CalendarAdapter(context, calendarViewsArrayList);
        displayEmptyEvents();
        displayHeaderDate(date);
        displayCalendarWithData();
        calendarUtils.setBackgroundDrawing(headerDate, backgroundDrawing);
        gridView.setAdapter(calendarAdapter);

    }


    private void displayEmptyEvents() {

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


    private void displayCalendarWithData() {

        calendarFill = setDateAtFirstDayOfAMonth();
        lastMondayOfCurrentMonth = lastMondayOfCurrentMonth(calendarFill);
        displayPeriodEveryMonth();
        calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar();
        //Wypełniam kalendarz
        while (calendarViewsArrayList.size() < DAYS_COUNT) {

            dayOfMonth = calendarFill.getDayOfMonth();
            displayEventsAndShiftsIfEmpty();

            if (periodStartDate != null) {
                displayCalendarWithPeriodData();

            } else {
                calendarViewsArrayList.add(new CalendarViews(setCalendarColor(), calendarFill, headerDate, dayOfMonth, shiftNumber, event));
            }
            calendarFill = calendarFill.plusDays(1);
        }

    }


    private LocalDate setDateAtFirstDayOfAMonth() {

        int year = calendarFill.getYear();
        Month month = calendarFill.getMonth();

        //Ustawiam datę na pierwszy dzień aktualnego miesiąca
        calendarFill = LocalDate.of(year, month, 1);

        return calendarFill;
    }


    private LocalDate lastMondayOfCurrentMonth(LocalDate localeDate) {

        LocalDate firstDayOfNextMonth = localeDate.plusMonths(1);
        int nextMonthBeginningCell =
                (firstDayOfNextMonth.getDayOfWeek().getValue()) - 1;
        return firstDayOfNextMonth.minusDays(nextMonthBeginningCell);
    }


    private void displayPeriodEveryMonth() {

        //Żeby dane na temat okresu wyświetlały się kiedy rozpocznie się następny miesiąc
        if (periodStartDate != null) {
            for (int i = 0; i < 1000; i++) {
                calendarFill = findWhatDateIsInTheFirstCellOfTheCalendar();
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


    private LocalDate findWhatDateIsInTheFirstCellOfTheCalendar() {

        //Sprawdzam, w której komórce kalendarza znajduje się pierwszy dzień miesiąca,
        //poprzez sprawdzenie odpowiadającego mu numeru dnia tygodnia. Odejmuję jeden, bo
        //poniedziałek ma numer 1, a komórki w ArrayList są naliczane od 0.
        int monthBeginningCell = (calendarFill.getDayOfWeek().getValue()) - 1;
        //Ustawiam datę na tę, która powinna się znaleźć w pierwszej komórce mojej ArrayList
        calendarFill = calendarFill.minusDays(monthBeginningCell);
        return calendarFill;
    }


    private void displayEventsAndShiftsIfEmpty() {

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


    private void displayCalendarWithPeriodData() {

        if ((periodStartDate.isEqual(calendarFill) ||
                periodFinishDate.isEqual(calendarFill)) ||
                (periodStartDate.isBefore(calendarFill) &&
                        periodFinishDate.isAfter(calendarFill))) {

            calendarViewsArrayList.add(new CalendarViews(setCalendarColor(),
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


                //TODO Jeżeli następny miesiąc obejmuje dwie linijki w widoku kalendarza,
                //a początek okresu występuje w obu tych linijkach, to na następnej stronie
                // kalendarza trzeba będzie wyświetlić pierwszy okres z pierwszej z tych
                // linijek i do niego dodawać długość cyklu
            }
        } else {

            calendarViewsArrayList.add(new CalendarViews(setCalendarColor(), calendarFill, headerDate,
                    dayOfMonth, shiftNumber, event));
        }

    }


    private void onGridViewItemClickListener() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (shiftsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    pasteShiftNumber(view, position);

                } else {

                    goToExpandedDayView(position);
                }

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
        newShiftNumber = (String) item.getText();
        TextView shiftNumber1 = view.findViewById(R.id.shiftNumber);
        shiftNumber1.setText(newShiftNumber);
        pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
        pickedDay = String.valueOf(pickedDate);
        saveShiftToPickedDate();

        ShiftsDao shiftsDao = getDatabase(context).shiftsDao();
        Shift shift = shiftsDao.findByShiftName(newShiftNumber);
        getAlarmTime(shift);
        if (alarmHour != null) {
            setAlarmToPickedDay(alarmHour, alarmMinute, pickedDate);
        }
    }


    private void saveShiftToPickedDate() {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents shiftToUpdate = calendarEventsDao.findBypickedDate(pickedDay);

        if (shiftToUpdate != null) {

            if (shiftToUpdate.getShiftNumber() != null) {
                if (!shiftToUpdate.getShiftNumber().equals(newShiftNumber)) {
                    shiftToUpdate.setShiftNumber(newShiftNumber);
                    calendarEventsDao.update(shiftToUpdate);

                }
            } else {
                shiftToUpdate.setShiftNumber(newShiftNumber);
                calendarEventsDao.update(shiftToUpdate);
            }

        } else {
            calendarEventsDao.insert(new CalendarEvents(newShiftNumber, "", pickedDay, String.valueOf(headerDate.getMonth().getValue())));
        }
    }


    private void getAlarmTime(Shift shift) {
        String alarm = shift.getAlarm();
        if (!alarm.equals("")) {
            String[] split = alarm.split(":");
            alarmHour = split[0];
            alarmMinute = split[1];
        }
    }


    private int uniqueRequestCodeForEachAlarm(LocalDate pickedDate) {

        int day = pickedDate.getDayOfMonth();
        int month = pickedDate.getMonthValue();
        int year = pickedDate.getYear();

        String findId = day + "" + month + "" + year;
        return Integer.parseInt(findId);
    }


    private void setAlarmToPickedDay(String hour, String minutes, LocalDate pickedDate) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), uniqueRequestCodeForEachAlarm(pickedDate), intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, pickedDateToMilis(hour, minutes, pickedDate),
                pendingIntent);
    }


    private void deleteAlarmFromAPickedDay(LocalDate pickedDate) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, uniqueRequestCodeForEachAlarm(pickedDate), intent,
                0);

        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }


    private long pickedDateToMilis(String hour, String minutes, LocalDate pickedDate) {

        LocalDateTime ldt = LocalDateTime.of(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minutes), 0);
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }


    private void goToExpandedDayView(int position) {

        pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
        pickedDay = String.valueOf(pickedDate);

        BackgroundActivityExpandedDayView eventsList = new BackgroundActivityExpandedDayView();
        Bundle args = new Bundle();
        args.putString("pickedDay", pickedDay);
        eventsList.setArguments(args);
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.flContent, eventsList).addToBackStack("tag").commit();
    }


    public void saveEventsNumberToPickedDate(String pickedDay) {

        EventsDao eventsDao = getDatabase(context).eventsDao();
        List<Event> listOfEvents = eventsDao.findByEventDate(pickedDay);
        int numberOfEvents = listOfEvents.size();

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents eventToUpdate = calendarEventsDao.findBypickedDate(pickedDay);

        if (eventToUpdate != null) {

            if (!eventToUpdate.getEventsNumber().equals(String.valueOf(numberOfEvents))) {
                eventToUpdate.setEventsNumber(String.valueOf(numberOfEvents));
                calendarEventsDao.update(eventToUpdate);

            }
        } else {
            calendarEventsDao.insert(new CalendarEvents(shiftNumber, String.valueOf(numberOfEvents), pickedDay, ""));
        }
    }


    private void deleteShiftFromPickedDate() {

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {

                TextView shiftNumber1 = childView.findViewById(R.id.shiftNumber);
                shiftNumber1.setText("");
                pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
                deleteAlarmFromAPickedDay(pickedDate);

                pickedDay = String.valueOf(pickedDate);

                CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
                CalendarEvents shiftToDelete = calendarEventsDao.findBypickedDate(pickedDay);
                if (shiftToDelete != null) {
                    calendarEventsDao.deleteBypickedDate(pickedDay);
                }


                return true;
            }
        });
    }


    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonthView();
            }
        });
    }


    private void setPreviousMonthView() {
        calendarUtils.flipAnimation(calendarCardView);
        //Odejmowanie miesiąca
        calendarFill = headerDate.minusMonths(1);
        displayPrewiousMonthPeriod();
        fillTheCalendar(context, date, backgroundDrawing, gridView);
    }


    private LocalDate previousMonthPeriodStartDate() {

        int year = calendarFill.getYear();
        int day = calendarFill.getDayOfMonth();
        Month month = calendarFill.getMonth();

        LocalDate firstDayOfPreviousMonth = LocalDate.of(year, month, 1);
        int previousMonthBeginningCell = (firstDayOfPreviousMonth.getDayOfWeek().getValue()) - 1;
        LocalDate firstDayOfPreviousMonthView = firstDayOfPreviousMonth.minusDays(previousMonthBeginningCell);

        for (int i = 0; i < 42; i++) {
            if (periodFinishDate.isAfter(firstDayOfPreviousMonthView)
                    || periodFinishDate.isEqual(firstDayOfPreviousMonthView)) {
                periodStartDate = periodStartDate.minusDays(cycleLenght);
                periodFinishDate = periodFinishDate.minusDays(cycleLenght);

                if (periodStartDate.isBefore(firstDayOfPreviousMonthView.minusDays(periodLenght))) {

                    periodStartDate = periodStartDate.plusDays(cycleLenght);
                    periodFinishDate = periodFinishDate.plusDays(cycleLenght);
                }
            }
        }
        return periodStartDate;

    }


    private void setNextButtonClickEvent() {

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonthView();

            }
        });
    }


    private void setNextMonthView() {
        calendarUtils.flipAnimation(calendarCardView);
        calendarFill = headerDate.plusMonths(1);
        fillTheCalendar(context, date, backgroundDrawing, gridView);
    }


    private void displayPrewiousMonthPeriod() {
        if (periodStartDate != null) {

            periodStartDate = previousMonthPeriodStartDate();
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

            fillTheCalendar(context, date, backgroundDrawing, gridView);
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();

        } else {

            periodStartDate = null;
            periodFinishDate = null;
            fillTheCalendar(context, date, backgroundDrawing, gridView);
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();
        }
    }


    void saveColor(int clickedOn) {

        colorsDao = getDatabase(context).colorsDao();
        colorToUpdate = colorsDao.findLastColor1();

        if (colorToUpdate != null) {
            if (colorToUpdate.getColor_number() != clickedOn) {
                colorToUpdate.setId(getId());
                colorToUpdate.setColor_number(clickedOn);
                colorsDao.update(colorToUpdate);

            }
        } else {
            colorsDao.insert(new Colors(getId(), clickedOn));
        }

        colorToUpdate = colorsDao.findLastColor1();
        calendarFill = LocalDate.now();
        loadPeriodData();

    }


    void deleteAllShifts(LocalDate date) {

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        calendarEventsDao.deleteAllShifts(String.valueOf(date.getMonth().getValue()));
        calendarFill = date;
        if (periodStartDate != null) {
            periodStartDate = previousMonthPeriodStartDate();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);
        }
    }


    private int setCalendarColor() {

        colorsDao = getDatabase(context).colorsDao();
        colorToUpdate = colorsDao.findLastColor1();

        int colorSettings;
        if (colorToUpdate == null) {
            colorSettings = 0;
        } else {
            colorSettings = colorToUpdate.getColor_number();
        }
        return colorSettings;
    }

}

