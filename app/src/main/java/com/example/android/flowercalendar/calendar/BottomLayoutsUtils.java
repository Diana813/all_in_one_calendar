package com.example.android.flowercalendar.calendar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.android.flowercalendar.shifts.ShiftsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

class BottomLayoutsUtils {


    static void checkShiftsLayoutState(BottomSheetBehavior shiftsSheetBehavior) {

        if (shiftsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    static void initShiftsData(CalendarFragment context, final BottomLayoutShiftsAdapter bottomLayoutShiftsAdapter) {
        ShiftsViewModel shiftsViewModel = new ViewModelProvider(context).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(context, bottomLayoutShiftsAdapter::setShiftList);
    }


    static void shiftsBottomSheetListener(final BottomSheetBehavior shiftsSheetBehavior, final ImageView imageView) {
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

    private static void hideShifts(ImageView shiftsDownArrow, final BottomSheetBehavior shiftsSheetBehavior) {
        shiftsDownArrow.setOnClickListener(view -> shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }

}
