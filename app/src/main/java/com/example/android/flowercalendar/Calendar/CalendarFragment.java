package com.example.android.flowercalendar.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.GestureInteractionsViews;
import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Shifts.ShiftsViewModel;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.PeriodDataDao;
import com.example.android.flowercalendar.database.Shift;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;


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
    private int clickedOn = 0;
    private BottomLayoutShiftsAdapter shiftsAdapter;
    private Context context;
    private String shiftNumber;
    private ColorsDao colorsDao;
    private Colors colorToUpdate;
    private String event;
    private ArrayList<CalendarEvents> viewsArrayList;
    private ArrayList<CalendarViews> calendarViewsArrayList;
    private String pickedDay;
    private LocalDate pickedDate;
    private int colorSettings;
    private int dayOfMonth;
    private int index;
    private String newShiftNumber;
    private DatabaseAdapter databaseAdapter;


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

        databaseAdapter = new DatabaseAdapter(getContext(), viewsArrayList);

        CardView bottom_sheet = rootView.findViewById(R.id.colorSettings);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        LinearLayout shifts_bottom_sheet = rootView.findViewById(R.id.shiftsSettings);
        shiftsSheetBehavior = BottomSheetBehavior.from(shifts_bottom_sheet);
        shiftsSheetBehavior.setHideable(true);
        shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        shiftsAdapter = new BottomLayoutShiftsAdapter(context, context);

        RecyclerView shifts_recycler_view = rootView.findViewById(R.id.shifts_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        shifts_recycler_view.setLayoutManager(layoutManager);
        shifts_recycler_view.setAdapter(shiftsAdapter);

        initShiftsData();
        red = (RelativeLayout) rootView.findViewById(R.id.red);
        yellow = (RelativeLayout) rootView.findViewById(R.id.yellow);
        green = (RelativeLayout) rootView.findViewById(R.id.green);
        blue = (RelativeLayout) rootView.findViewById(R.id.blue);
        violet = (RelativeLayout) rootView.findViewById(R.id.violet);
        grey = (RelativeLayout) rootView.findViewById(R.id.grey);
        colorSettingsDownArrow = (ImageView) rootView.findViewById(R.id.colorSettingsDownArrow);
        shiftsDownArrow = (ImageView) rootView.findViewById(R.id.shiftsDownArrow);

        setHasOptionsMenu(true);
        gridView = rootView.findViewById(R.id.gridView);
        onGridViewItemClickListener();
        swipeTheCalendar();
        initCalendarData();

        date = (TextView) rootView.findViewById(R.id.date);
        previousButton = (ImageView) rootView.findViewById(R.id.calendar_prev_button);
        nextButton = (ImageView) rootView.findViewById(R.id.calendar_next_button);
        calendarCardView = (CardView) rootView.findViewById(R.id.calendarCardView);
        loadPeriodData();

        return rootView;
    }


    private void initShiftsData() {
        ShiftsViewModel shiftsViewModel = ViewModelProviders.of(this).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(this, new Observer<List<Shift>>() {
            @Override
            public void onChanged(@Nullable List<Shift> shifts) {
                shiftsAdapter.setShiftList(shifts);

            }

        });
    }

    private void initCalendarData() {
        CalendarViewModel calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        calendarViewModel.getEventsList().observe(this, new Observer<List<CalendarEvents>>() {
            @Override
            public void onChanged(@Nullable List<CalendarEvents> events) {
                databaseAdapter.setEvetsList(events);

            }

        });
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
        if (item.getItemId() == R.id.settings) {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            bottomSheetListener();
            return true;
        }
        if (item.getItemId() == R.id.settingShifts) {

            if (shiftsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            shiftsBottomSheetListener();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void bottomSheetListener() {

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        setBlue();
                        setGreen();
                        setRed();
                        setYellow();
                        setViolet();
                        setGrey();
                        hideColorSettings();
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


    }

    private void shiftsBottomSheetListener() {
        shiftsSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        hideShifts();
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    private void setRed() {

        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 1;
                initShiftsData();
                saveColor();
            }

        });


    }

    private void setYellow() {

        yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 2;
                initShiftsData();
                saveColor();
            }
        });
    }

    private void setGreen() {

        green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 3;
                initShiftsData();
                saveColor();
            }
        });
    }

    private void setBlue() {

        blue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 4;
                initShiftsData();
                saveColor();
            }
        });
    }

    private void setViolet() {

        violet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 5;
                initShiftsData();
                saveColor();
            }
        });
    }

    private void setGrey() {

        grey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 6;
                initShiftsData();
                saveColor();
            }
        });
    }

    private void hideColorSettings() {
        colorSettingsDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void hideShifts() {
        shiftsDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }


    private void fillTheCalendar() {

        calendarViewsArrayList = new ArrayList<>();
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), calendarViewsArrayList);

        if (shiftNumber == null) {
            shiftNumber = "";
        }

        if (event == null) {
            event = "";
        }

        colorsDao = CalendarDatabase.getDatabase(context).colorsDao();
        colorToUpdate = colorsDao.findLastColor1();

        if (colorToUpdate == null) {
            colorSettings = 0;
        } else {
            colorSettings = colorToUpdate.getColor_number();
        }
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

        //Ustawiam datę na pierwszy dzień aktualnego miesiąca
        calendarFill = LocalDate.of(year, month, 1);

        //Zmienne potrzebne do prawidłowego wyświetlania zdarzeń cyklicznych w widoku kalendarza
        LocalDate firstDayOfNextMonth = calendarFill.plusMonths(1);
        int nextMonthBeginningCell =
                (firstDayOfNextMonth.getDayOfWeek().getValue()) - 1;
        LocalDate mondayBeforeFirstDayOfNextMonth
                = firstDayOfNextMonth.minusDays(nextMonthBeginningCell);

        //Sprawdzam, w której komórce kalendarza znajduje się pierwszy dzień miesiąca,
        //poprzez sprawdzenie odpowiadającego mu numeru dnia tygodnia. Odejmuję jeden, bo
        //poniedziałek ma numer 1, a komórki w ArrayList są naliczane od 0.
        int monthBeginningCell = (calendarFill.getDayOfWeek().getValue()) - 1;

        //Ustawiam datę na tę, która powinna się znaleźć w pierwszej komórce mojej ArrayList
        calendarFill = calendarFill.minusDays(monthBeginningCell);

        //Wypełniam kalendarz
        while (calendarViewsArrayList.size() < DAYS_COUNT) {

            dayOfMonth = calendarFill.getDayOfMonth();

            if (periodStartDate != null) {


                if ((periodStartDate.isEqual(calendarFill) ||
                        periodFinishDate.isEqual(calendarFill)) ||
                        (periodStartDate.isBefore(calendarFill) &&
                                periodFinishDate.isAfter(calendarFill))) {

                    calendarViewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate, periodStartDate,
                            periodFinishDate, dayOfMonth, shiftNumber,
                            event, R.mipmap.period_icon_v2));

                    //Kiedy w kalendarzu wypełni się komórka z ostatnim dniem okresu
                    //trzeba dodać długość cyklu do daty początkowej i do daty końcowej okresu,
                    //ale tylko pod warunkiem, że ten okres nie powinien wyświetlić się na
                    //następnej stronie kalendarza. Jeśli musi się wyświetlić, data tego okresu powinna
                    //zostać taka jak była
                    if (periodFinishDate.isEqual(calendarFill) &&
                            periodFinishDate.isBefore(mondayBeforeFirstDayOfNextMonth)) {

                        periodStartDate = periodStartDate.plusDays(cycleLenght);
                        periodFinishDate = periodFinishDate.plusDays(cycleLenght);


                        //TODO Jeżeli następny miesiąc obejmuje dwie linijki w widoku kalendarza,
                        //a początek okresu występuje w obu tych linijkach, to na następnej stronie
                        // kalendarza trzeba będzie wyświetlić pierwszy okres z pierwszej z tych
                        // linijek i do niego dodawać długość cyklu
                    }
                } else {

                    calendarViewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate,
                            dayOfMonth, shiftNumber, event));
                }

            } else {
                calendarViewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate, dayOfMonth, shiftNumber, event));
            }


            calendarFill = calendarFill.plusDays(1);
        }


        gridView.setAdapter(calendarAdapter);

    }

    private void onGridViewItemClickListener() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (shiftsSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

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

                    index = position;

                    if (view == null)
                        return;


                    assert item != null;
                    newShiftNumber = (String) item.getText();
                    TextView shiftNumber1 = view.findViewById(R.id.shiftNumber);
                    shiftNumber1.setText(newShiftNumber);
                   /* String calendarFillString = viewsArrayList.get(position).getCalendarFillString();
                    String[] parts = calendarFillString.split("-");
                    int yearCalendarFill = Integer.parseInt(parts[0]);
                    int monthCalendarFill = Integer.parseInt(parts[1]);
                    int dayCalendarFill = Integer.parseInt(parts[2]);*/
                    pickedDate = calendarViewsArrayList.get(position).getmCalendarFill();
                    //pickedDate = LocalDate.of(yearCalendarFill, monthCalendarFill, dayCalendarFill);
                    int day = pickedDate.getDayOfMonth();
                    int month = pickedDate.getMonth().getValue();
                    int year = pickedDate.getYear();
                    pickedDay = day + ":" + month + ":" + year;
                    saveShiftToPickedDate();


                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }

        });

    }

    private void saveShiftToPickedDate() {

        int id = 0;
        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
        CalendarEvents shiftToUpdate = calendarEventsDao.findBypickedDate(pickedDay);


        if (shiftToUpdate != null) {
            if (!shiftToUpdate.getShiftNumber().equals(shiftNumber)) {
                shiftToUpdate.setId(getId());
                shiftToUpdate.setShiftNumber(shiftNumber);
                calendarEventsDao.update(shiftToUpdate);

            }
        } else {
            calendarEventsDao.insert(new CalendarEvents(colorSettings, String.valueOf(calendarFill), String.valueOf(headerDate), String.valueOf(periodStartDate), String.valueOf(periodFinishDate), dayOfMonth, shiftNumber, "", R.mipmap.period_icon_v2, pickedDay));

        }

    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipAnimation();
                //Odejmowanie miesiąca
                calendarFill = headerDate.minusMonths(1);

                if (periodStartDate != null) {

                    periodStartDate = previousMonthPeriodStartDate();
                    periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

                }

                //TODO prawidłowe wyświetlanie zdarzeń z poprzedniego miesiąca
                fillTheCalendar();
            }
        });
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
                flipAnimation();
                calendarFill = headerDate.plusMonths(1);
                fillTheCalendar();

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipeTheCalendar() {

        gridView.setOnTouchListener(new GestureInteractionsViews(getContext()) {

            public void onSwipeTop() {
            }

            public void onSwipeRight() {

                flipAnimation();
                calendarFill = headerDate.plusMonths(1);
                fillTheCalendar();

            }

            public void onSwipeLeft() {

                flipAnimation();
                //Odejmowanie miesiąca
                calendarFill = headerDate.minusMonths(1);

                if (periodStartDate != null) {

                    periodStartDate = previousMonthPeriodStartDate();
                    periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

                }

                //TODO prawidłowe wyświetlanie zdarzeń z poprzedniego miesiąca
                fillTheCalendar();


            }

            public void onSwipeBottom() {
            }

        });
    }

    //Animacja obrotu kalendarza
    private void flipAnimation() {

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(calendarCardView, "scaleX", 1f, 0f);
        final ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(calendarCardView, "scaleX", 0f, 1f);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                objectAnimator2.start();
            }
        });
        objectAnimator.start();
    }


    private void loadPeriodData() {

        calendarFill = LocalDate.now();

        PeriodDataDao periodDataDao = CalendarDatabase.getDatabase(context).periodDataDao();
        if(periodDataDao.findLastPeriod() != null){
            String periodStart = periodDataDao.findLastPeriod().getPeriodStartDate();
            String[] parts = periodStart.split(":");

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);

            periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            periodLenght = periodDataDao.findLastPeriod().getPeriodLength();
            cycleLenght = periodDataDao.findLastPeriod().getCycleLength();
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

            fillTheCalendar();
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();

        }else{

            periodStartDate = null;
            periodFinishDate = null;
            fillTheCalendar();
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();
        }


    }

    private void saveColor() {

        colorsDao = CalendarDatabase.getDatabase(context).colorsDao();
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

}

