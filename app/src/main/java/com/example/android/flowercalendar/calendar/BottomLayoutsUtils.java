package com.example.android.flowercalendar.calendar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.shifts.ShiftsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

class BottomLayoutsUtils {

    void checkColorLayoutState(BottomSheetBehavior sheetBehavior) {

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    void checkShiftsLayoutState(BottomSheetBehavior shiftsSheetBehavior) {

        if (shiftsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    void initShiftsData(CalendarFragment context, final BottomLayoutShiftsAdapter bottomLayoutShiftsAdapter) {
        ShiftsViewModel shiftsViewModel = new ViewModelProvider(context).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(context, bottomLayoutShiftsAdapter::setShiftList);
    }

    private void setRed(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {
        layout.setOnClickListener(v -> {
            int clickedOn = 1;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });
    }

    private void setYellow(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {
        layout.setOnClickListener(v -> {
            int clickedOn = 2;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });

    }

    private void setGreen(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {

        layout.setOnClickListener(v -> {
            int clickedOn = 3;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });

    }

    private void setBlue(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {

        layout.setOnClickListener(v -> {
            int clickedOn = 4;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });

    }

    private void setViolet(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {

        layout.setOnClickListener(v -> {
            int clickedOn = 5;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });
    }

    private void setGrey(RelativeLayout layout, final BottomLayoutShiftsAdapter shiftsAdapter, final CalendarFragment calendarFragment) {

        layout.setOnClickListener(v -> {
            int clickedOn = 6;
            initShiftsData(calendarFragment, shiftsAdapter);
            calendarFragment.saveColor(clickedOn);
        });
    }


    private void hideColorSettings(ImageView colorSettingsDownArrow, final BottomSheetBehavior sheetBehavior) {
        colorSettingsDownArrow.setOnClickListener(view -> sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }

    void shiftsBottomSheetListener(final BottomSheetBehavior shiftsSheetBehavior, final ImageView imageView) {
        shiftsSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        hideShifts(imageView, shiftsSheetBehavior);
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

    private void hideShifts(ImageView shiftsDownArrow, final BottomSheetBehavior shiftsSheetBehavior) {
        shiftsDownArrow.setOnClickListener(view -> shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }

    void bottomSheetListener(final BottomSheetBehavior sheetBehavior, final ImageView colorSettingsDownArrow, final RelativeLayout red, final RelativeLayout yellow, final RelativeLayout green, final RelativeLayout blue, final RelativeLayout violet, final RelativeLayout grey, final Context context, final CalendarFragment calendarFragment) {

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        BottomLayoutShiftsAdapter shiftsAdapter = new BottomLayoutShiftsAdapter(context);
                        setRed(red, shiftsAdapter,
                                calendarFragment);
                        setYellow(yellow, shiftsAdapter,
                                calendarFragment);
                        setGreen(green, shiftsAdapter,
                                calendarFragment);
                        setBlue(blue, shiftsAdapter,
                                calendarFragment);
                        setViolet(violet, shiftsAdapter,
                                calendarFragment);
                        setGrey(grey, shiftsAdapter,
                                calendarFragment);
                        hideColorSettings(colorSettingsDownArrow, sheetBehavior);
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


}
