package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.BottomLayoutShiftsAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class BottomLayoutsUtils {


    public static void checkBottomLayoutState(BottomSheetBehavior sheetBehavior, SharedPreferences sharedPreferences, Context context) {

        boolean showShifts;
        if (sharedPreferences != null) {
            showShifts = sharedPreferences.getBoolean(context.getString(R.string.show_shifts), false);
        } else {
            showShifts = true;
        }

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED && showShifts) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public static void initShiftsData(MainActivity context, final BottomLayoutShiftsAdapter bottomLayoutShiftsAdapter, TextView emptyView) {
        ShiftsViewModel shiftsViewModel = new ViewModelProvider(context).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(context,
                shiftList -> {
                    bottomLayoutShiftsAdapter.setShiftList(shiftList);
                    if (shiftList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText(R.string.no_shifts);
                    }
                }
        );

    }


    public static void bottomSheetListener(final BottomSheetBehavior sheetBehavior, final View imageView) {
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        hideBottomLayout(imageView, sheetBehavior);
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

    private static void hideBottomLayout(View shiftsDownArrow, final BottomSheetBehavior sheetBehavior) {
        if (shiftsDownArrow != null) {
            shiftsDownArrow.setOnClickListener(view -> sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
        }
    }

}
