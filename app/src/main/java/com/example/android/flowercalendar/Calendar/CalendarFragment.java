package com.example.android.flowercalendar.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Shifts.ShiftsViewModel;
import com.example.android.flowercalendar.data.Contract;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.ColorsDatabase;
import com.example.android.flowercalendar.database.Shift;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.CONTENT_URI;


public class CalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri periodDataUri;
    private static final int PERIOD_DATA_LOADER = 0;
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
    private ArrayList<CalendarViews> viewsArrayList;
    private String pickedDay;
    private LocalDate pickedDate;
    private int colorSettings;
    private int dayOfMonth;
    private int index;
    private String newShiftNumber;


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

        date = (TextView) rootView.findViewById(R.id.date);
        previousButton = (ImageView) rootView.findViewById(R.id.calendar_prev_button);
        nextButton = (ImageView) rootView.findViewById(R.id.calendar_next_button);
        calendarCardView = (CardView) rootView.findViewById(R.id.calendarCardView);
        periodDataUri = CONTENT_URI;
        getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);
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

        viewsArrayList = new ArrayList<CalendarViews>();
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), viewsArrayList);

        if (shiftNumber == null) {
            shiftNumber = "";
        }

        if (event == null) {
            event = "";
        }

        colorsDao = ColorsDatabase.getDatabase(context).colorsDao();
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
        while (viewsArrayList.size() < DAYS_COUNT) {

            dayOfMonth = calendarFill.getDayOfMonth();

            if (periodStartDate != null) {


                if ((periodStartDate.isEqual(calendarFill) ||
                        periodFinishDate.isEqual(calendarFill)) ||
                        (periodStartDate.isBefore(calendarFill) &&
                                periodFinishDate.isAfter(calendarFill))) {

                    viewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate, periodStartDate,
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

                    viewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate, dayOfMonth, shiftNumber, event));
                }

            } else {
                viewsArrayList.add(new CalendarViews(colorSettings, calendarFill, headerDate, dayOfMonth, shiftNumber, event));
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
                    pickedDate = viewsArrayList.get(position).getmCalendarFill();
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

    //TODO zamienić Loader na ViewModels i LiveData.
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                Contract.PeriodDataEntry._ID,
                Contract.PeriodDataEntry.COLUMN_START_DATE,
                Contract.PeriodDataEntry.COLUMN_PERIOD_LENGHT,
                Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT,
        };

        return new CursorLoader(Objects.requireNonNull(getActivity()), periodDataUri,
                projection, null, null, null);

    }


    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            calendarFill = LocalDate.now();
            fillTheCalendar();
            setNextButtonClickEvent();
            setPreviousButtonClickEvent();
        }

        if (cursor.moveToFirst()) {

            int idColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry._ID);
            int periodStartColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_START_DATE);
            int periodLenghtColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_PERIOD_LENGHT);
            int cycleLenghtColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT);

            int currentID = cursor.getInt(idColumnIndex);
            String periodStart = cursor.getString(periodStartColumnIndex);
            periodLenght = cursor.getInt(periodLenghtColumnIndex);
            cycleLenght = cursor.getInt(cycleLenghtColumnIndex);
            String[] parts = periodStart.split(":");

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);

            periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            periodFinishDate = periodStartDate.plusDays(periodLenght - 1);

            calendarFill = LocalDate.now();
            fillTheCalendar();
            setPreviousButtonClickEvent();
            setNextButtonClickEvent();

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private void saveColor() {

        colorsDao = ColorsDatabase.getDatabase(context).colorsDao();
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
        getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);

    }


}

