package com.example.android.flowercalendar.ForGirls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.PeriodData;
import com.example.android.flowercalendar.database.PeriodDataDao;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ForGirlsFragment extends Fragment {

    public ForGirlsFragment() {
        // Required empty public constructor
    }

    private CalendarView mCalendarView;
    private String startPeriodDate;
    private TextView periodTimeValue;
    private TextView cycleTimeValue;
    private int periodTimeChosenValue;
    private int cycleTimeChosenValue;
    private SeekBar periodTimeSeekBar;
    private SeekBar cycleTimeSeekBar;
    private PeriodDataDao periodDataDao;


    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean periodDataHasChanged = true;
            return false;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_for_girls, container, false);

        setHasOptionsMenu(true);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.edit_period_data_activity_label));


        mCalendarView = (CalendarView)
                rootView.findViewById(R.id.girlsCalendar);
        periodTimeSeekBar = (SeekBar) rootView.findViewById(R.id.periodTime);
        cycleTimeSeekBar = (SeekBar) rootView.findViewById(R.id.cycleTime);
        mCalendarView.setOnTouchListener(touchListener);
        periodTimeSeekBar.setOnTouchListener(touchListener);
        cycleTimeSeekBar.setOnTouchListener(touchListener);
        periodTimeValue = (TextView) rootView.findViewById(R.id.periodTimeValue);
        cycleTimeValue = (TextView) rootView.findViewById(R.id.cycleTimeValue);

        setPeriodStartDate();
        setPeriodTimeValue();
        setCycleTimeValue();
        initData();
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_delete_menu, menu);
        MenuItem item = menu.findItem(R.id.save);
        item.setVisible(false);
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

    private void setPeriodStartDate() {
        mCalendarView
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth) {
                                startPeriodDate = year + ":" + (month + 1) + ":" + dayOfMonth;

                            }
                        });

    }

    private void setPeriodTimeValue() {

        periodTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                periodTimeValue.setText("" + progress);
                //noinspection IntegerDivisionInFloatingPointContext
                periodTimeValue.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                periodTimeChosenValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void setCycleTimeValue() {

        cycleTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                cycleTimeValue.setText("" + progress);
                //noinspection IntegerDivisionInFloatingPointContext
                cycleTimeValue.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                cycleTimeChosenValue = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
    }

    private void deleteLastPeriod() {
        periodDataDao = CalendarDatabase.getDatabase(getContext()).periodDataDao();
        PeriodData periodToDelete = periodDataDao.findLastPeriod();
        if (periodToDelete != null) {
            periodDataDao.deleteLastPeriod();
        }

        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        long currentZonedDateTimeInMilis = currentZonedDateTime.toInstant().toEpochMilli();
        periodTimeSeekBar.setProgress(0);
        cycleTimeSeekBar.setProgress(0);
        mCalendarView.setDate(currentZonedDateTimeInMilis);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteLastPeriod();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void saveData() {

        periodDataDao = CalendarDatabase.getDatabase(getContext()).periodDataDao();
        PeriodData periodToUpdate = periodDataDao.findLastPeriod();

        if (periodToUpdate != null) {
            if (!periodToUpdate.getPeriodStartDate().equals(startPeriodDate)) {
                if (startPeriodDate == null) {
                    periodToUpdate.setPeriodStartDate(periodToUpdate.getPeriodStartDate());
                } else {
                    periodToUpdate.setPeriodStartDate(startPeriodDate);

                }
                periodToUpdate.setPeriodLength(periodTimeChosenValue);
                periodToUpdate.setCycleLength(cycleTimeChosenValue);

                periodDataDao.update(periodToUpdate);

            }
        } else {
            periodDataDao.insert(new PeriodData(startPeriodDate, periodTimeChosenValue, cycleTimeChosenValue));
        }
    }

    private void initData() {

        periodDataDao = CalendarDatabase.getDatabase(getContext()).periodDataDao();

        if (periodDataDao.findLastPeriod() == null) {

            ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
            long currentZonedDateTimeInMilis = currentZonedDateTime.toInstant().toEpochMilli();
            periodTimeSeekBar.setProgress(0);
            cycleTimeSeekBar.setProgress(0);
            mCalendarView.setDate(currentZonedDateTimeInMilis);
        } else {
            String periodStartDay = periodDataDao.findLastPeriod().getPeriodStartDate();
            String[] parts = periodStartDay.split(":");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            LocalDateTime periodStartDate = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day);
            ZonedDateTime zdt = periodStartDate.atZone(ZoneId.systemDefault());
            long periodStartDateInMilis = zdt.toInstant().toEpochMilli();
            mCalendarView.setDate(periodStartDateInMilis);

            int periodLength = periodDataDao.findLastPeriod().getPeriodLength();
            periodTimeSeekBar.setProgress(periodLength);

            int cycleLength = periodDataDao.findLastPeriod().getCycleLength();
            cycleTimeSeekBar.setProgress(cycleLength);

        }
    }

}

