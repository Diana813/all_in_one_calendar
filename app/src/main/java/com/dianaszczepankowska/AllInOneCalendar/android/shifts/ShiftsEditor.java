package com.dianaszczepankowska.AllInOneCalendar.android.shifts;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.saveShiftData;


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
    private TextInputEditText shiftNameEditText;
    private TextInputEditText shiftLengthEditText;
    public static int newShiftLength;
    private TextView resetAlarm;
    private Button confirm;
    private TextView clean;
    private LinearLayout startLayout;
    private LinearLayout alarmButton;

    public static ShiftsEditor newInstance(int id, String shiftName, String shift_start, String alarm, Integer shift_length) {

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
        handleToolbarAndBottomMenu();
        findViews(view);
        startLayout.setOnClickListener(v -> shiftSettingDialog());
        setConfirmButton();
        setCleanButton();
        alarmButton.setOnClickListener(v -> alarmSettingDialog());
        setResetAlarm();

        if (!shift_name_extra.equals("-1") &&
                shift_alarm_extra != null &&
                shift_schedule_extra != null) {
            shiftNameEditText.setText(shift_name_extra);
            shiftNameEditText.setSelection(shift_name_extra.length());
            shiftStartTextView.setText(shift_schedule_extra);
            alarmTextView.setText(shift_alarm_extra);
            shiftLengthEditText.setText(shift_length_extra);
            shiftStartTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void handleToolbarAndBottomMenu() {
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.add_shift));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(R.drawable.baseline_chevron_left_black_24);
        MainActivity.menu.findItem(R.id.events).setIcon(R.drawable.baseline_business_center_black_48).setChecked(true).setOnMenuItemClickListener(item -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new ShiftsFragment()).addToBackStack("tag").commit();
            return true;
        });
    }


    private void findViews(View view) {
        shiftLengthEditText = view.findViewById(R.id.shift_lenght_edit_text);
        shiftNameEditText = view.findViewById(R.id.textinput);
        shiftStartTextView = view.findViewById(R.id.shiftStart);
        resetAlarm = view.findViewById(R.id.reset);
        confirm = view.findViewById(R.id.confirmButton);
        clean = view.findViewById(R.id.clean);
        alarmTextView = view.findViewById(R.id.alarmStart);
        startLayout = view.findViewById(R.id.startLayout);
        alarmButton = view.findViewById(R.id.alarm_button);

    }

    private void shiftSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> shiftStartTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
        shiftStartTextView.setVisibility(View.VISIBLE);

    }

    private void alarmSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> alarmTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();

    }


    private void setConfirmButton() {

        confirm.setOnClickListener(v -> {
            saveShiftData(shiftNameEditText, shiftStartTextView, alarmTextView, shiftLengthEditText, context, shift_name_extra);
            DialogsUtils.hideKeyboard(getView(), context);
            goBackToShifts();
        });
    }


    private void goBackToShifts() {

        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new ShiftsFragment());
        fragmentTransaction.commitNow();
    }

    @SuppressLint("SetTextI18n")
    private void setResetAlarm() {
        resetAlarm.setOnClickListener(v -> alarmTextView.setText(""));
    }

    private void setCleanButton() {
        clean.setOnClickListener(v -> {
            shiftNameEditText.setText("");
            shiftStartTextView.setText("");
            shiftLengthEditText.setText("");
            alarmTextView.setText("");
        });
    }
}
