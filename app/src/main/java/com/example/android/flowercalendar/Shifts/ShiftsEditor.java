package com.example.android.flowercalendar.Shifts;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.example.android.flowercalendar.Shifts.ShiftsFragment.newId;


public class ShiftsEditor extends Fragment {

    private Context context;
    private String shift_name_extra;
    private String shift_schedule_extra;
    private String shift_alarm_extra;
    private String shift_length_extra;
    private static final String EXTRA_SHIFT_NAME = "shift_name";
    private static final String EXTRA_SHIFT_ID = "id";
    private static final String EXTRA_SHIFT_SCHEDULE = "shift_start_time";
    private static final String EXTRA_SHIFT_ALARM = "shift_alarm";
    private static final String EXTRA_SHIFT_LENGHT = "shift_length";

    private TextView shiftStartTextView;
    private TextView alarmTextView;
    private EditText shiftNameEditText;
    private EditText shiftLengthEditText;
    private int newShiftLength;

    static ShiftsEditor newInstance(int id, String shiftName, String shift_start, String alarm, Integer shift_length) {

        ShiftsEditor fragment = new ShiftsEditor();
        Bundle args = new Bundle();
        args.putInt(EXTRA_SHIFT_ID, id);
        args.putString(EXTRA_SHIFT_NAME, shiftName);
        args.putString(EXTRA_SHIFT_SCHEDULE, shift_start);
        args.putString(EXTRA_SHIFT_ALARM, alarm);
        args.putString(EXTRA_SHIFT_LENGHT, String.valueOf(shift_length));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        assert args != null;
        shift_name_extra = args.getString(EXTRA_SHIFT_NAME);
        String shift_id_extra = args.getString(EXTRA_SHIFT_ID);
        shift_schedule_extra = args.getString(EXTRA_SHIFT_SCHEDULE);
        shift_alarm_extra = args.getString(EXTRA_SHIFT_ALARM);
        shift_length_extra = args.getString(EXTRA_SHIFT_LENGHT);

        if (shift_name_extra == null) {
            shift_name_extra = "-1";
        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shift_editor_activity, container, false);

        setHasOptionsMenu(true);

        shiftLengthEditText = view.findViewById(R.id.shift_lenght_edit_text);

        shiftNameEditText = view.findViewById(R.id.shift_name_edit_text);
        shiftStartTextView = (TextView) view.findViewById(R.id.shiftStart);
        ImageView shiftStartSettingButton = (ImageView) view.findViewById(R.id.schedule_button);
        shiftStartSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftSettingDialog();
            }
        });

        alarmTextView = (TextView) view.findViewById(R.id.alarmStart);
        ImageView alarmButton = (ImageView) view.findViewById(R.id.alarm_button);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingDialog();
            }
        });

        if (!shift_name_extra.equals("-1") &&
                shift_alarm_extra != null &&
                shift_schedule_extra != null) {
            shiftNameEditText.setText(shift_name_extra);
            shiftNameEditText.setSelection(shift_name_extra.length());
            shiftStartTextView.setText(shift_schedule_extra);
            alarmTextView.setText(shift_alarm_extra);
            shiftLengthEditText.setText(shift_length_extra);
        }


        return view;
    }


    private void saveShift() {

        String newShiftName = shiftNameEditText.getText().toString();
        String newShiftStart = shiftStartTextView.getText().toString();
        String newAlarm = alarmTextView.getText().toString();


        try {
            newShiftLength = Integer.parseInt(shiftLengthEditText.getText().toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();

        }

        if (newShiftName.isEmpty() &&
                newAlarm.isEmpty() && newShiftStart.isEmpty()) {
            return;
        }


        ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();

        if (!shift_name_extra.equals("-1")) {

            Shift shiftToUpdate = shiftsDao.findByShiftName(shift_name_extra);
            if (shiftToUpdate != null) {
                if ((!shiftToUpdate.getShift_name().equals(newShiftName)) ||
                        (!shiftToUpdate.getSchedule().equals(newShiftStart)) ||
                        (!shiftToUpdate.getAlarm().equals(newAlarm)) ||
                        (shiftToUpdate.getShift_length() != newShiftLength)) {
                    shiftToUpdate.setShift_name(newShiftName);
                    shiftToUpdate.setSchedule(newShiftStart);
                    shiftToUpdate.setAlarm(newAlarm);
                    shiftToUpdate.setShift_length(newShiftLength);
                    shiftsDao.update(shiftToUpdate);
                }
            }
        } else {

            shiftsDao.insert(new Shift(newId, newShiftName, newShiftStart, newAlarm, newShiftLength));

        }
    }

    private void shiftSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {

                        shiftStartTextView.setText(String.format("%02d:%02d", hour, minute));

                    }
                }, 0, 0, true);
        timePickerDialog.show();
    }

    private void alarmSettingDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {
                        alarmTextView.setText(String.format("%02d:%02d", hour, minute));

                    }
                }, 0, 0, true);
        timePickerDialog.show();
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
                saveShift();

                final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);

                FragmentTransaction fragmentTransaction =
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new ShiftsFragment());
                fragmentTransaction.commitNow();

                return true;

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
                if (!shift_name_extra.equals("-1")) {
                    // clicked on item row -> delete
                    Shift shiftToDelete = shiftsDao.findByShiftName(shift_name_extra);
                    if (shiftToDelete != null) {
                        shiftsDao.deleteByShiftName(shift_name_extra);
                    }

                    FragmentTransaction fragmentTransaction =
                            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.flContent, new ShiftsFragment());
                    fragmentTransaction.commitNow();

                }
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
}
