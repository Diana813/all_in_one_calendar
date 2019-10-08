package com.example.android.flowercalendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.data.Contract;

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
    private GridView gridView;
    private static final int DAYS_COUNT = 42;
    private ImageView previousButton;
    private ImageView nextButton;
    private TextView date;
    private int cycleLenght;
    private LocalDate periodStartDate;
    private LocalDate periodFinishDate;
    private LocalDate calendarFill;
    private CardView calendarCardView;
    private LocalDate headerDate;


    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_calendar, container, false);

        setHasOptionsMenu(true);
        gridView = rootView.findViewById(R.id.gridView);
        date = (TextView) rootView.findViewById(R.id.date);
        previousButton = (ImageView) rootView.findViewById(R.id.calendar_prev_button);
        nextButton = (ImageView) rootView.findViewById(R.id.calendar_next_button);
        calendarFill = LocalDate.now();
        calendarCardView = (CardView) rootView.findViewById(R.id.calendarCardView);
        periodDataUri = CONTENT_URI;
        getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);
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
        return super.onOptionsItemSelected(item);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    private void fillTheCalendar() {

        ArrayList<CalendarViews> views = new ArrayList<CalendarViews>();

        //Data wyświetlena w górnym TextView
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

                    views.add(new CalendarViews(calendarFill, headerDate, periodStartDate,
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

                    views.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, 0, "event"));
                }
            } else {
                views.add(new CalendarViews(calendarFill, headerDate, dayOfMonth, 0, "event"));
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
                //TODO prawidłowe wyświetlanie zdarzeń z poprzedniego miesiąca
                fillTheCalendar();
            }
        });
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
                Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT
        };
        return new CursorLoader(Objects.requireNonNull(getActivity()), periodDataUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Cursor cursor) {
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
            int periodLenght = cursor.getInt(periodLenghtColumnIndex);
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
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

        //TODO dopisać co ma robić przy resecie
    }

}