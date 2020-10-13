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

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.MainActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
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

import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.saveWorkAndEventData;

public class AddEventFragment extends Fragment {

    private Context context;
    private String event_name_extra;
    private String event_schedule_extra;
    private String event_notification_extra;
    private String event_length_extra;
    private static final String EXTRA_EVENT_NAME = "event_name";
    private static final String EXTRA_EVENT_ID = "id";
    private static final String EXTRA_EVENT_SCHEDULE = "event_start_time";
    private static final String EXTRA_EVENT_ALARM = "event_alarm";
    private static final String EXTRA_EVENT_LENGHT = "event_length";

    private TextView eventStartTextView;
    private TextView notificationTextView;
    private TextInputEditText eventNameEditText;
    private TextInputEditText eventLengthEditText;
    public int newEventLength;
    private TextView resetAlarm;
    private Button confirm;
    private TextView clean;
    private LinearLayout startLayout;
    private LinearLayout alarmButton;

    public static AddEventFragment newInstance(int id, String eventName, String event_start, String alarm, Integer event_length) {

        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_EVENT_ID, id);
        args.putString(EXTRA_EVENT_NAME, eventName);
        args.putString(EXTRA_EVENT_SCHEDULE, event_start);
        args.putString(EXTRA_EVENT_ALARM, alarm);
        args.putString(EXTRA_EVENT_LENGHT, String.valueOf(event_length));
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
            event_name_extra = args.getString(EXTRA_EVENT_NAME);
            event_schedule_extra = args.getString(EXTRA_EVENT_SCHEDULE);
            event_notification_extra = args.getString(EXTRA_EVENT_ALARM);
            event_length_extra = args.getString(EXTRA_EVENT_LENGHT);
        }

        if (event_name_extra == null) {
            event_name_extra = "-1";
        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shift_editor_activity, container, false);

        setHasOptionsMenu(true);
        handleToolbarAndBottomMenu();
        findViews(view);
        startLayout.setOnClickListener(v -> eventStartSettingDialog());
        setConfirmButton();
        setCleanButton();
        alarmButton.setOnClickListener(v -> alarmSettingDialog());
        setResetAlarm();

        if (!event_name_extra.equals("-1") &&
                event_notification_extra != null &&
                event_schedule_extra != null) {
            eventNameEditText.setText(event_name_extra);
            eventNameEditText.setSelection(event_name_extra.length());
            eventStartTextView.setText(event_schedule_extra);
            notificationTextView.setText(event_notification_extra);
            eventLengthEditText.setText(event_length_extra);
            eventStartTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void handleToolbarAndBottomMenu() {
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.add_event));
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setSubtitle(null);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setIcon(R.drawable.baseline_chevron_left_black_24);
        MainActivity.menu.findItem(R.id.events).setIcon(R.drawable.baseline_today_black_48).setChecked(true).setOnMenuItemClickListener(item -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            BackgroundActivityExpandedDayView backgroundActivityExpandedDayView = new BackgroundActivityExpandedDayView();
            Bundle args = new Bundle();
            args.putString("pickedDay", currentDate);
            backgroundActivityExpandedDayView.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.flContent, backgroundActivityExpandedDayView).addToBackStack("tag").commit();
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
        eventLengthEditText = view.findViewById(R.id.shift_lenght_edit_text);
        eventNameEditText = view.findViewById(R.id.textinput);
        eventStartTextView = view.findViewById(R.id.shiftStart);
        resetAlarm = view.findViewById(R.id.reset);
        resetAlarm.setText(R.string.resetNotification);
        confirm = view.findViewById(R.id.confirmButton);
        clean = view.findViewById(R.id.clean);
        notificationTextView = view.findViewById(R.id.alarmStart);
        startLayout = view.findViewById(R.id.startLayout);
        alarmButton = view.findViewById(R.id.alarm_button);

    }

    private void eventStartSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> eventStartTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
        eventStartTextView.setVisibility(View.VISIBLE);

    }

    private void alarmSettingDialog() {

        @SuppressLint({"DefaultLocale", "SetTextI18n"}) TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                (view, hour, minute) -> notificationTextView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();

    }


    private void setConfirmButton() {

        confirm.setOnClickListener(v -> {
            saveWorkAndEventData(eventNameEditText, eventStartTextView, notificationTextView, eventLengthEditText, context, event_name_extra, EventKind.EVENTS.getIntValue(), newEventLength);
            DialogsUtils.hideKeyboard(getView(), context);
            goBackToExpandedDayView();
        });
    }


    private void goBackToExpandedDayView() {

        FragmentTransaction fragmentTransaction =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        BackgroundActivityExpandedDayView backgroundActivityExpandedDayView = new BackgroundActivityExpandedDayView();
        Bundle args = new Bundle();
        args.putString("pickedDay", currentDate);
        backgroundActivityExpandedDayView.setArguments(args);
        fragmentTransaction.replace(R.id.flContent, backgroundActivityExpandedDayView);
        fragmentTransaction.commitNow();
    }

    @SuppressLint("SetTextI18n")
    private void setResetAlarm() {
        resetAlarm.setOnClickListener(v -> notificationTextView.setText(""));
    }

    private void setCleanButton() {
        clean.setOnClickListener(v -> {
            eventNameEditText.setText("");
            eventStartTextView.setText("");
            eventLengthEditText.setText("");
            notificationTextView.setText("");
        });
    }
}