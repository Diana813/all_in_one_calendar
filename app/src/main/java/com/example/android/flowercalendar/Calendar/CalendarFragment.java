package com.example.android.flowercalendar.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.flowercalendar.Calendar.CalendarAdapter;
import com.example.android.flowercalendar.Calendar.CalendarViews;
import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.data.Contract;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.CONTENT_URI;


public class CalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri periodDataUri;
    private static final int PERIOD_DATA_LOADER = 0;
    private static final int COLOR_DATA_LOADER = 1;
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
    private int colorSettings;
    private BottomSheetBehavior sheetBehavior;
    private RelativeLayout red;
    private RelativeLayout blue;
    private RelativeLayout yellow;
    private RelativeLayout green;
    private RelativeLayout violet;
    private RelativeLayout grey;
    private Uri colorDataUri;
    private int clickedOn = 0;


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_calendar, container, false);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();

        if (intent.getExtras() == null) {
            colorSettings = 0;
        } else if (intent.getExtras() != null) {
            colorSettings = intent.getExtras().getInt("colorSettings");
        }

        CardView bottom_sheet = rootView.findViewById(R.id.colorSettings);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        red = (RelativeLayout) rootView.findViewById(R.id.red);
        yellow = (RelativeLayout) rootView.findViewById(R.id.yellow);
        green = (RelativeLayout) rootView.findViewById(R.id.green);
        blue = (RelativeLayout) rootView.findViewById(R.id.blue);
        violet = (RelativeLayout) rootView.findViewById(R.id.violet);
        grey = (RelativeLayout) rootView.findViewById(R.id.grey);
        colorDataUri = Contract.ColorDataEntry.CONTENT_URI;


        setHasOptionsMenu(true);
        gridView = rootView.findViewById(R.id.gridView);
        date = (TextView) rootView.findViewById(R.id.date);
        previousButton = (ImageView) rootView.findViewById(R.id.calendar_prev_button);
        nextButton = (ImageView) rootView.findViewById(R.id.calendar_next_button);
        calendarFill = LocalDate.now();
        calendarCardView = (CardView) rootView.findViewById(R.id.calendarCardView);
        periodDataUri = CONTENT_URI;
        getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);
        getLoaderManager().initLoader(COLOR_DATA_LOADER, null, this);
        return rootView;
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void bottomSheetListener() {

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
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
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

    public void setRed() {

        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 1;
                saveData();
            }

        });


    }

    private void setYellow() {

        yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 2;
                saveData();
            }
        });
    }

    public void setGreen() {

        green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 3;
                saveData();
            }
        });
    }

    private void setBlue() {

        blue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 4;
                saveData();
            }
        });
    }

    private void setViolet() {

        violet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 5;
                saveData();
            }
        });
    }

    private void setGrey() {

        grey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 6;
                saveData();
            }
        });
    }


    private void fillTheCalendar() {

        ArrayList<CalendarViews> views = new ArrayList<CalendarViews>();

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
        while (views.size() < DAYS_COUNT) {

            int dayOfMonth = calendarFill.getDayOfMonth();

            if (periodStartDate != null) {


                if ((periodStartDate.isEqual(calendarFill) ||
                        periodFinishDate.isEqual(calendarFill)) ||
                        (periodStartDate.isBefore(calendarFill) &&
                                periodFinishDate.isAfter(calendarFill))) {

                    views.add(new CalendarViews(colorSettings, calendarFill, headerDate, periodStartDate,
                            periodFinishDate, dayOfMonth, 0,
                            "event", R.mipmap.period_icon_v2));

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

                    views.add(new CalendarViews(colorSettings, calendarFill, headerDate, dayOfMonth, 0, "event"));
                }
            } else {
                views.add(new CalendarViews(colorSettings, calendarFill, headerDate, dayOfMonth, 0, "event"));
            }

            calendarFill = calendarFill.plusDays(1);
        }


        CalendarAdapter adapter = new CalendarAdapter(getContext(), views);
        gridView.setAdapter(adapter);
    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flipAnimation();
                //Odejmowanie miesiąca
                calendarFill = headerDate.minusMonths(1);

                periodStartDate = previousMonthPeriodStartDate();
                periodFinishDate = periodStartDate.plusDays(periodLenght -1 );
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

        if (id == 0) {
            String[] projection = {
                    Contract.PeriodDataEntry._ID,
                    Contract.PeriodDataEntry.COLUMN_START_DATE,
                    Contract.PeriodDataEntry.COLUMN_PERIOD_LENGHT,
                    Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT,
            };

            return new CursorLoader(Objects.requireNonNull(getActivity()), periodDataUri,
                    projection, null, null, null);
        } else {
            String[] projection = {
                    Contract.ColorDataEntry._ID,
                    Contract.ColorDataEntry.COLOR_NUMBER
            };
            return new CursorLoader(Objects.requireNonNull(getActivity()), colorDataUri,
                    projection, null, null, null);
        }

    }


    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor cursor) {
        switch (loader.getId()) {
            case 0:
                if (cursor == null || cursor.getCount() < 1) {
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
                    fillTheCalendar();
                    setPreviousButtonClickEvent();
                    setNextButtonClickEvent();

                }
                break;
            case 1:
                if (cursor == null || cursor.getCount() < 1) {

                    colorDataUri = null;
                    colorSettings = 0;
                    calendarFill = LocalDate.now();
                    getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);
                } else {
                    colorDataUri = Contract.ColorDataEntry.CONTENT_URI;
                }
                assert cursor != null;
                if (cursor.moveToFirst()) {

                    int idColumnIndex = cursor.getColumnIndex(Contract.ColorDataEntry._ID);
                    int colorColumnIndex = cursor.getColumnIndex(Contract.ColorDataEntry.COLOR_NUMBER);

                    int currentID = cursor.getInt(idColumnIndex);

                    colorSettings = cursor.getInt(colorColumnIndex);
                    calendarFill = LocalDate.now();
                    getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);
                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    private void saveData() {

        if (colorDataUri == null &&
                Integer.parseInt(String.valueOf(clickedOn)) == 0) {
            return;
        }

        final ContentValues values = new ContentValues();

        values.put(Contract.ColorDataEntry.COLOR_NUMBER, clickedOn);

        if (colorDataUri == null) {

            Uri newUri = Objects.requireNonNull(getActivity()).getContentResolver().insert(Contract.ColorDataEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(getContext(), getString(R.string.insert_data_error),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = Objects.requireNonNull(getActivity()).getContentResolver().update(colorDataUri, values,
                    null, null);
            if (rowsAffected == 0) {

                Toast.makeText(getContext(), getString(R.string.edit_data_error),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}

