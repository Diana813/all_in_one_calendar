package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.BuildConfig;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.coworkers.CoworkersUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.coworkers.FriendsPicker;
import com.dianaszczepankowska.AllInOneCalendar.android.holidaysData.Holiday;
import com.dianaszczepankowska.AllInOneCalendar.android.holidaysData.HolidaysLoader;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.confirm;

public class CalendarFrame extends Fragment implements LoaderManager.LoaderCallbacks<List<Holiday>> {

    private static final String API_KEY = BuildConfig.API_KEY;
    public Context context;
    public static BottomSheetBehavior shiftsSheetBehavior;
    public LocalDate headerDate;
    public TextView date;
    public LinearLayout backgroundDrawing;
    public GridView gridView;
    private String holiday;
    private static String country = Locale.getDefault().getCountry().substring(0, 2).toUpperCase();
    public static List<Holiday> holidays;
    private boolean showShifts;
    private Menu menu;
    public static SharedPreferences sharedPreferences;
    private CalendarFragment calendarFragment;

    private static final String HOLIDAYS_REQUEST_URL = "https://www.googleapis.com/calendar/v3/calendars/pl.polish%23holiday%40group.v.calendar.google.com/events?";


    static final int HOLIDAY_LOADER_ID = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        calendarFragment = new CalendarFragment();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_fragment_menu, menu);
        this.menu = menu;
        showShifts = sharedPreferences.getBoolean(getString(R.string.show_shifts), false);
        if (showShifts) {
            menu.findItem(R.id.showShifts).setChecked(true);

        } else {
            menu.findItem(R.id.showShifts).setChecked(false);
            greyMenuItem(menu.findItem(R.id.deleteShifts));
            greyMenuItem(menu.findItem(R.id.settingShifts));
            greyMenuItem(menu.findItem(R.id.share));

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settingShifts) {
            BottomLayoutsUtils.checkBottomLayoutState(shiftsSheetBehavior, sharedPreferences, context);
            BottomLayoutsUtils.bottomSheetListener(shiftsSheetBehavior, confirm);
            return true;
        }

        if (item.getItemId() == R.id.deleteShifts) {
            if (showShifts) {
                CalendarUtils.showDeleteConfirmationDialog(context, headerDate, date, gridView);
            }
            return true;
        }

        if (item.getItemId() == R.id.share) {
            if (showShifts) {
                FragmentManager manager = getChildFragmentManager();
                FriendsPicker dialog = new FriendsPicker();
                dialog.show(manager, null);
                CoworkersUtils.showFriendsList();
                CoworkersUtils.shareDataWithFriend();
            }
            return true;
        }

        if (item.getItemId() == R.id.showShifts) {
            showShifts = sharedPreferences.getBoolean(getString(R.string.show_shifts), false);

            if (!showShifts) {
                menu.findItem(R.id.showShifts).setChecked(true);
                showShifts = true;

            } else {
                menu.findItem(R.id.showShifts).setChecked(false);
                showShifts = false;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.show_shifts), showShifts);
            editor.apply();
            FragmentTransaction fragmentTransaction = (Objects.requireNonNull(getActivity())).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, calendarFragment);
            fragmentTransaction.commitNow();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void greyMenuItem(MenuItem item) {
        SpannableString s = new SpannableString(item.getTitle().toString());
        s.setSpan(new ForegroundColorSpan(context.getColor(R.color.lightGreyZilla)), 0, s.length(), 0);
        item.setTitle(s);

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

        //;
       /* uriBuilder.appendQueryParameter("api_key", key);
        uriBuilder.appendQueryParameter("country", country);
        uriBuilder.appendQueryParameter("year", year);
        uriBuilder.appendQueryParameter("day", day);
        uriBuilder.appendQueryParameter("month", month);
        uriBuilder.appendQueryParameter("language", country.toLowerCase());*/

        uriBuilder.appendQueryParameter("key", API_KEY);

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
