package com.example.android.flowercalendar.calendar;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFrame extends Fragment {

    public Context context;
    public BottomLayoutShiftsAdapter shiftsAdapter;
    public BottomSheetBehavior shiftsSheetBehavior;
    public ImageView shiftsDownArrow;
    public LocalDate headerDate;
    public TextView date;
    public LinearLayout backgroundDrawing;
    public GridView gridView;
    public LinearLayout shifts_bottom_sheet;
    public RecyclerView shifts_recycler_view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        shiftsAdapter = new BottomLayoutShiftsAdapter(context);
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

        if (item.getItemId() == R.id.settingShifts) {
            BottomLayoutsUtils.checkShiftsLayoutState(shiftsSheetBehavior);
            BottomLayoutsUtils.shiftsBottomSheetListener(shiftsSheetBehavior, shiftsDownArrow);
            return true;
        }

        if (item.getItemId() == R.id.deleteShifts) {
            CalendarUtils.showDeleteConfirmationDialog(context, headerDate, date, gridView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void launchLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void setBottomSheetsBehavior() {
        shiftsSheetBehavior = BottomSheetBehavior.from(shifts_bottom_sheet);
        shiftsSheetBehavior.setHideable(true);
        shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    public void setBottomLayoutShiftsAdapter() {

        shiftsAdapter = new BottomLayoutShiftsAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        shifts_recycler_view.setLayoutManager(layoutManager);
        shifts_recycler_view.setAdapter(shiftsAdapter);
    }

}
