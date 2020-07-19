package com.dianaszczepankowska.AllInOneCalendar.android.shifts;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsFragment.newId;


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
        shiftStartTextView = view.findViewById(R.id.shiftStart);
        ImageView shiftStartSettingButton = view.findViewById(R.id.event_time_button);
        shiftStartSettingButton.setOnClickListener(v -> shiftSettingDialog());

        alarmTextView = view.findViewById(R.id.alarmStart);
        ImageView alarmButton = view.findViewById(R.id.alarm_button);
        alarmButton.setOnClickListener(v -> alarmSettingDialog());

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
                if (shiftToUpdate.getAlarm() != null && !shiftToUpdate.getAlarm().equals(newAlarm)) {
                    CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
                    List<CalendarEvents> changedAlarmShiftsList = calendarEventsDao.findByShiftNumber(shiftToUpdate.getShift_name());

                    String[] parts = shiftToUpdate.getAlarm().split(":");
                    String alarmHour = parts[0];
                    String alarmMinute = parts[1];

                    String[] parts2 = newAlarm.split(":");
                    String newAlarmHour = parts2[0];
                    String newAlarmMinute = parts2[1];

                    if (changedAlarmShiftsList != null) {
                        for (CalendarEvents calendarEvents : changedAlarmShiftsList) {
                            AlarmUtils.deleteAlarmFromAPickedDay(AppUtils.refactorStringIntoDate(calendarEvents.getPickedDate()), alarmHour, alarmMinute, context, ACTION_OPEN_ALARM_CLASS);

                            AlarmUtils.setAlarmToPickedDay(newAlarmHour, newAlarmMinute, AppUtils.refactorStringIntoDate(calendarEvents.getPickedDate()), context, "Open alarm class");
                        }
                    }
                }


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

        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hour, minute) -> shiftStartTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
    }

    private void alarmSettingDialog() {

        @SuppressLint("DefaultLocale") TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hour, minute) -> alarmTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            saveShift();
            AppUtils.hideKeyboard(getView(), context);
            goBackToShifts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void goBackToShifts() {

        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new ShiftsFragment());
        fragmentTransaction.commitNow();
    }
}
