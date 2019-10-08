package com.example.android.flowercalendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.example.android.flowercalendar.data.Contract;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT;
import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.COLUMN_PERIOD_LENGHT;
import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.COLUMN_START_DATE;
import static com.example.android.flowercalendar.data.Contract.PeriodDataEntry.CONTENT_URI;

public class ForGirlsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ForGirlsFragment() {
        // Required empty public constructor
    }

    private static final int PERIOD_DATA_LOADER = 0;

    private CalendarView mCalendarView;
    private String startPeriodDate;
    private boolean periodDataHasChanged = false;
    private Uri periodDataUri;
    private TextView periodTimeValue;
    private TextView cycleTimeValue;
    private int periodTimeChosenValue;
    private int cycleTimeChosenValue;
    private SeekBar periodTimeSeekBar;
    private SeekBar cycleTimeSeekBar;


    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            periodDataHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_for_girls, container, false);

        setHasOptionsMenu(true);

        periodDataUri = CONTENT_URI;
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.edit_period_data_activity_label));
        getLoaderManager().initLoader(PERIOD_DATA_LOADER, null, this);


        mCalendarView = (CalendarView)
                rootView.findViewById(R.id.girlsCalendar);
        periodTimeSeekBar = (SeekBar) rootView.findViewById(R.id.periodTime);
        cycleTimeSeekBar = (SeekBar) rootView.findViewById(R.id.cycleTime);
        mCalendarView.setOnTouchListener(touchListener);
        periodTimeSeekBar.setOnTouchListener(touchListener);
        cycleTimeSeekBar.setOnTouchListener(touchListener);
        periodTimeValue = (TextView) rootView.findViewById(R.id.periodTimeValue);
        cycleTimeValue = (TextView) rootView.findViewById(R.id.cycleTimeValue);

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

        periodTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                periodTimeValue.setText("" + progress);
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

        cycleTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                cycleTimeValue.setText("" + progress);
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


        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.for_girls_save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                saveData();
                return true;

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;

            //TODO pokazać showUnsavedChangesDialog przy przechodzeniu do innego fragmentu,
            //jeżeli dane zostały zmienione, ale nie zapisane

        /*        if (!periodDataHasChanged) {
                    NavUtils.navigateUpFromSameTask(Objects.requireNonNull(getActivity()));
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(Objects.requireNonNull(getActivity()));
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    //TODO pokazać showUnsavedChangesDialog po naciśnięciu Back buttona
   /* @Override
    public void onBackPressed() {
        if (!periodDataHasChanged) {
            super.getActivity().onBackPressed();
            return;
        } else {
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavUtils.navigateUpFromSameTask(Objects.requireNonNull(getActivity()));
                        }
                    };

            showUnsavedChangesDialog(discardButtonClickListener);
        }
    }*/

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.unsaved_changes_dialog_message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllData() {
        int rowsDeleted = Objects.requireNonNull(getContext()).getContentResolver().delete(Contract.PeriodDataEntry.CONTENT_URI, null, null);
        Log.v("ForGirlsFragment", rowsDeleted + " rows deleted from period database");
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllData();
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

        if (periodDataUri == null &&
                TextUtils.isEmpty(startPeriodDate) &&
                Integer.parseInt(String.valueOf(periodTimeChosenValue)) == 0 &&
                Integer.parseInt(String.valueOf(cycleTimeChosenValue)) == 0) {
            return;
        }

        final ContentValues values = new ContentValues();

        if (periodDataUri != null &&
                TextUtils.isEmpty(startPeriodDate) ||
                Integer.parseInt(String.valueOf(periodTimeChosenValue)) == 0 ||
                Integer.parseInt(String.valueOf(cycleTimeChosenValue)) == 0) {
            Toast.makeText(getContext(), getString(R.string.Empty_field),
                    Toast.LENGTH_SHORT).show();
        } else {

            values.put(COLUMN_START_DATE, startPeriodDate);
            values.put(COLUMN_PERIOD_LENGHT, periodTimeChosenValue);
            values.put(COLUMN_CYCLE_LENGHT, cycleTimeChosenValue);


            if (periodDataUri == null) {

                Uri newUri = Objects.requireNonNull(getActivity()).getContentResolver().insert(CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(getContext(), getString(R.string.insert_data_error),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.data_saved),
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = Objects.requireNonNull(getActivity()).getContentResolver().update(periodDataUri, values,
                        null, null);
                if (rowsAffected == 0) {

                    Toast.makeText(getContext(), getString(R.string.edit_data_error),
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(getContext(), getString(R.string.data_edited),
                            Toast.LENGTH_SHORT).show();
                }
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            periodDataUri = null;
            Objects.requireNonNull(getActivity()).setTitle(getString(R.string.add_period_data_activity_label));
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
            onLoaderReset(loader);
            return;
        } else {
            periodDataUri = CONTENT_URI;
        }

        if (cursor.moveToFirst()) {

            int idColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry._ID);
            int periodStartColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_START_DATE);
            int periodLenghtColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_PERIOD_LENGHT);
            int cycleLenghtColumnIndex = cursor.getColumnIndex(Contract.PeriodDataEntry.COLUMN_CYCLE_LENGHT);

            int currentID = cursor.getInt(idColumnIndex);
            String periodStart = cursor.getString(periodStartColumnIndex);
            int periodLenght = cursor.getInt(periodLenghtColumnIndex);
            int cycleLenght = cursor.getInt(cycleLenghtColumnIndex);
            String[] parts = periodStart.split(":");

            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            LocalDateTime periodStartDate = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day);

            ZonedDateTime zdt = periodStartDate.atZone(ZoneId.systemDefault());
            long periodStartDateInMilis = zdt.toInstant().toEpochMilli();

            mCalendarView.setDate(periodStartDateInMilis);
            periodTimeSeekBar.setProgress(periodLenght);
            cycleTimeSeekBar.setProgress(cycleLenght);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

        ZonedDateTime currentZonedDateTime = ZonedDateTime.now();
        long currentZonedDateTimeInMilis = currentZonedDateTime.toInstant().toEpochMilli();
        periodTimeSeekBar.setProgress(0);
        cycleTimeSeekBar.setProgress(0);
        mCalendarView.setDate(currentZonedDateTimeInMilis);

    }
}

