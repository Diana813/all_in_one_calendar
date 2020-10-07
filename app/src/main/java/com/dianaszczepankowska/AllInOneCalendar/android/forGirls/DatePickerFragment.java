package com.dianaszczepankowska.AllInOneCalendar.android.forGirls;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DatePickerFragment extends Fragment {

    private CalendarView mCalendarView;
    private String startPeriodDate;
    private Button confirmButton;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener touchListener = (view, motionEvent) -> false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.date_picker_layout, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.periodStartDate));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(R.drawable.baseline_chevron_left_black_24);

        mCalendarView = rootView.findViewById(R.id.girlsCalendar);
        confirmButton = rootView.findViewById(R.id.confirmButton);
        confirmButton.setText(R.string.add);

        setPeriodStartDate();
        setConfirmButton();
        return rootView;
    }

    @SuppressLint("DefaultLocale")
    private void setPeriodStartDate() {
        mCalendarView
                .setOnDateChangeListener(
                        (view, year, month, dayOfMonth) -> startPeriodDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));

    }

    private void setConfirmButton() {
        confirmButton.setOnClickListener(v -> {
            Fragment editorFragment = ForGirlsFragment.newInstance(startPeriodDate);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, editorFragment).addToBackStack(null).commit();
        });
    }


}