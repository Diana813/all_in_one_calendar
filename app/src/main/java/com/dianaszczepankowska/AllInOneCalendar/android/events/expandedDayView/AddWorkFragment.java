package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

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
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.saveWorkData;

public class AddWorkFragment extends Fragment {

    private Context context;
    private String work_name_extra;
    private String work_schedule_extra;
    private String work_notification_extra;
    private String work_length_extra;
    private static final String EXTRA_WORK_NAME = "work_name";
    private static final String EXTRA_WORK_ID = "id";
    private static final String EXTRA_WORK_SCHEDULE = "work_start_time";
    private static final String EXTRA_WORK_ALARM = "work_alarm";
    private static final String EXTRA_WORK_LENGHT = "work_length";

    private TextView workStartTextView;
    private TextView notificationTextView;
    private TextInputEditText workNameEditText;
    private TextInputEditText workLengthEditText;
    public static int newWorkLength;
    private TextView resetAlarm;
    private Button confirm;
    private TextView clean;
    private LinearLayout startLayout;
    private LinearLayout alarmButton;

    public static AddWorkFragment newInstance(int id, String shiftName, String shift_start, String alarm, Integer shift_length) {

        AddWorkFragment fragment = new AddWorkFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_WORK_ID, id);
        args.putString(EXTRA_WORK_NAME, shiftName);
        args.putString(EXTRA_WORK_SCHEDULE, shift_start);
        args.putString(EXTRA_WORK_ALARM, alarm);
        args.putString(EXTRA_WORK_LENGHT, String.valueOf(shift_length));
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
        if (args != null) {
            work_name_extra = args.getString(EXTRA_WORK_NAME);
            work_schedule_extra = args.getString(EXTRA_WORK_SCHEDULE);
            work_notification_extra = args.getString(EXTRA_WORK_ALARM);
            work_length_extra = args.getString(EXTRA_WORK_LENGHT);
        }

        if (work_name_extra == null) {
            work_name_extra = "-1";
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

        if (!work_name_extra.equals("-1") &&
                work_notification_extra != null &&
                work_schedule_extra != null) {
            workNameEditText.setText(work_name_extra);
            workNameEditText.setSelection(work_name_extra.length());
            workStartTextView.setText(work_schedule_extra);
            notificationTextView.setText(work_notification_extra);
            workLengthEditText.setText(work_length_extra);
            workStartTextView.setVisibility(View.VISIBLE);
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
        TextView newEventLabel = view.findViewById(R.id.shift_name);
        newEventLabel.setText(R.string.newEvent);
        TextInputLayout eventLengthLabel = view.findViewById(R.id.shift_lenght);
        eventLengthLabel.setHint(context.getString(R.string.duration));
        TextView alarmLabel = view.findViewById(R.id.shift_alarm);
        alarmLabel.setText(R.string.notification);
        workLengthEditText = view.findViewById(R.id.shift_lenght_edit_text);
        workNameEditText = view.findViewById(R.id.textinput);
        workStartTextView = view.findViewById(R.id.shiftStart);
        resetAlarm = view.findViewById(R.id.reset);
        resetAlarm.setText(R.string.resetNotification);
        confirm = view.findViewById(R.id.confirmButton);
        clean = view.findViewById(R.id.clean);
        notificationTextView = view.findViewById(R.id.alarmStart);
        startLayout = view.findViewById(R.id.startLayout);
        alarmButton = view.findViewById(R.id.alarm_button);

    }

    private void shiftSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> workStartTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
        workStartTextView.setVisibility(View.VISIBLE);

    }

    private void alarmSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> notificationTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();

    }


    private void setConfirmButton() {

        confirm.setOnClickListener(v -> {
            saveWorkData(workNameEditText, workStartTextView, notificationTextView, workLengthEditText, context, work_name_extra);
            DialogsUtils.hideKeyboard(getView(), context);
            goBackToExpandedDayView();
        });
    }


    private void goBackToExpandedDayView() {

        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new BackgroundActivityExpandedDayView());
        fragmentTransaction.commitNow();
    }

    @SuppressLint("SetTextI18n")
    private void setResetAlarm() {
        resetAlarm.setOnClickListener(v -> notificationTextView.setText(""));
    }

    private void setCleanButton() {
        clean.setOnClickListener(v -> {
            workNameEditText.setText("");
            workStartTextView.setText("");
            workLengthEditText.setText("");
            notificationTextView.setText("");
        });
    }
}
