package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.LoginActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.holidaysData.Holiday;
import com.dianaszczepankowska.AllInOneCalendar.android.holidaysData.HolidaysLoader;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFrame extends Fragment implements LoaderManager.LoaderCallbacks<List<Holiday>> {

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
    private String holiday;
    private static String country = Locale.getDefault().getCountry().substring(0, 2).toUpperCase();
    public static List<Holiday> holidays;

    private static final String HOLIDAYS_REQUEST_URL = "https://www.googleapis.com/calendar/v3/calendars/pl.polish%23holiday%40group.v.calendar.google.com/events?";
    //"https://calendarific.com/api/v2/holidays";

    static final int HOLIDAY_LOADER_ID = 1;

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

    @Override
    public Loader<List<Holiday>> onCreateLoader(int id, Bundle args) {

       /* String[] data = pickedDay.split("-");
        String year = data[0];
        String month = data[1].replaceAll("^0+(?=.)", "");
        String day = data[2].replaceAll("^0+(?=.)", "");*/


        Uri baseUri = Uri.parse(HOLIDAYS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //en.uk%40holiday.calendar.google.com/events?key=yourAPIKey";

        String apiKey = "AIzaSyCki6_qJzZOsWBSfIkYQ7LhZ1u4qflqTSA"; //"0882ca90415097ad93cc06c34075d971c03922e4";
       /* uriBuilder.appendQueryParameter("api_key", apiKey);
        uriBuilder.appendQueryParameter("country", country);
        uriBuilder.appendQueryParameter("year", year);
        uriBuilder.appendQueryParameter("day", day);
        uriBuilder.appendQueryParameter("month", month);
        uriBuilder.appendQueryParameter("language", country.toLowerCase());*/

        uriBuilder.appendQueryParameter("key", apiKey);

        return new HolidaysLoader(this.getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Holiday>> loader, List<Holiday> data) {
        if (data != null && !data.isEmpty()) {
            holidays = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Holiday>> loader) {

    }
}
