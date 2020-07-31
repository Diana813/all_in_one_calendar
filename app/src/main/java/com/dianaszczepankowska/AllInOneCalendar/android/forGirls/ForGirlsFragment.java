package com.dianaszczepankowska.AllInOneCalendar.android.forGirls;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;
import com.dianaszczepankowska.AllInOneCalendar.android.statistics.PeriodStatistics;
import com.dianaszczepankowska.AllInOneCalendar.android.widget.WidgetUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    private Button saveButon;
    private ImageView saveImage;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener touchListener = (view, motionEvent) -> false;

    @Override
    public void onPause() {
        super.onPause();
        WidgetUtils.updateWidget(getContext());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_for_girls, container, false);

        setHasOptionsMenu(true);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.edit_period_data_activity_label));


        mCalendarView = rootView.findViewById(R.id.girlsCalendar);
        periodTimeSeekBar = rootView.findViewById(R.id.periodTime);
        cycleTimeSeekBar = rootView.findViewById(R.id.cycleTime);
        mCalendarView.setOnTouchListener(touchListener);
        periodTimeSeekBar.setOnTouchListener(touchListener);
        cycleTimeSeekBar.setOnTouchListener(touchListener);
        periodTimeValue = rootView.findViewById(R.id.periodTimeValue);
        cycleTimeValue = rootView.findViewById(R.id.cycleTimeValue);
        saveButon = rootView.findViewById(R.id.save);
        saveImage = rootView.findViewById(R.id.saveImage);

        setPeriodStartDate();
        setPeriodTimeValue();
        setCycleTimeValue();
        initData();
        setSaveButon();
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_delete_all_entries).setTitle(getString(R.string.delete_last));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog();
            return true;
        } else if (item.getItemId() == R.id.statistics) {
            PeriodStatistics periodStatistics = new PeriodStatistics();
            FragmentTransaction tx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            tx.replace(((ViewGroup) Objects.requireNonNull(getView()).getParent()).getId(), periodStatistics);
            tx.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("DefaultLocale")
    private void setPeriodStartDate() {
        mCalendarView
                .setOnDateChangeListener(
                        (view, year, month, dayOfMonth) -> {
                            startPeriodDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                            saveImage.setVisibility(View.GONE);
                            saveButon.setVisibility(View.VISIBLE);
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
                saveImage.setVisibility(View.GONE);
                saveButon.setVisibility(View.VISIBLE);
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
                saveImage.setVisibility(View.GONE);
                saveButon.setVisibility(View.VISIBLE);

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
        initData();

    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.delete_last));
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deleteLastPeriod());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void saveData() {

        periodDataDao = CalendarDatabase.getDatabase(getContext()).periodDataDao();
        PeriodData periodToUpdate = periodDataDao.findLastPeriod();
        if (periodToUpdate != null) {
            if (startPeriodDate == null) {
                startPeriodDate = periodToUpdate.getPeriodStartDate();
            }
            if (periodTimeChosenValue == 0) {
                periodTimeChosenValue = periodToUpdate.getPeriodLength();
            }
            if (cycleTimeChosenValue == 0) {
                cycleTimeChosenValue = periodToUpdate.getCycleLength();
            }
        }

        if (periodToUpdate != null
                && (!periodToUpdate.getPeriodStartDate().equals(startPeriodDate))) {

            periodDataDao.insert(new PeriodData(startPeriodDate, periodTimeChosenValue, cycleTimeChosenValue));

        } else if (periodToUpdate != null
                && (periodToUpdate.getPeriodStartDate().equals(startPeriodDate)
                && (periodToUpdate.getPeriodLength() != periodTimeChosenValue
                || periodToUpdate.getCycleLength() != cycleTimeChosenValue))) {

            periodToUpdate.setPeriodLength(periodTimeChosenValue);
            periodToUpdate.setCycleLength(cycleTimeChosenValue);
            periodDataDao.update(periodToUpdate);

        } else if (periodToUpdate == null) {

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
            if (periodStartDay == null) {
                return;
            }
            String[] parts;
            if (periodStartDay.contains(":")) {
                parts = periodStartDay.split(":");
            } else {
                parts = periodStartDay.split("-");
            }
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

    private void setSaveButon() {
        saveButon.setOnClickListener(v -> {
            saveData();
            saveButon.setVisibility(View.GONE);
            saveImage.setVisibility(View.VISIBLE);
        });
    }

}

