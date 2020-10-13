package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.EventKind;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.BottomLayoutsUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.confirm;
import static com.dianaszczepankowska.AllInOneCalendar.android.MainActivity.confirmButton;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.shiftsSheetBehaviorExpandedDayView;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.findEventEndTime;

public class WorkCalendarShiftListAdapter extends RecyclerView.Adapter<WorkCalendarShiftListAdapter.WorkCalendarShiftsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private Event event;
    private int eventPosition;
    private String eventName;
    private ArrayList<String> eventNames = new ArrayList<>();


    public WorkCalendarShiftListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        WorkCalendarShiftListAdapter.context = context;

    }

    public static Context getContext() {
        return context;
    }


    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public WorkCalendarShiftListAdapter.WorkCalendarShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.event_list_item, parent, false);
        return new WorkCalendarShiftsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull WorkCalendarShiftListAdapter.WorkCalendarShiftsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }

        final Event event = eventsList.get(position);
        if (event != null) {
            holder.shiftLinearLayout.setPadding(0, 0, 0, 0);
            holder.shiftName.setText(event.getEvent_name());

            String eventEnd = findEventEndTime(event.getSchedule(), event.getEvent_length());
            if (event.getSchedule() != null && !eventEnd.equals("") && !event.getSchedule().equals("")) {
                holder.shiftSchedule.setText(event.getSchedule() + " - " + eventEnd);
            } else if (event.getSchedule() != null && !event.getSchedule().equals("")) {
                holder.shiftSchedule.setText(event.getSchedule());
            } else {
                holder.shiftSchedule.setVisibility(View.GONE);
            }

            if (event.getAlarm() == null || event.getAlarm().equals("")) {
                holder.shiftAlarm.setText("");
            } else {
                holder.shiftAlarm.setText(event.getAlarm());

            }

        }

        holder.itemView.setOnClickListener(v -> {
            BottomLayoutsUtils.checkBottomLayoutState(shiftsSheetBehaviorExpandedDayView, context.getApplicationContext());
            BottomLayoutsUtils.bottomSheetListener(shiftsSheetBehaviorExpandedDayView, confirmButton);
        });


    }


    public void deleteItem(int position) {

        event = eventsList.get(position);
        eventPosition = position;
        eventName = event.getEvent_name();
        eventNames.add(eventName);
        eventsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    public void deleteFromDatabase() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();

        if (eventNames != null) {
            for (int i = 0; i < eventNames.size(); i++) {
                eventsDao.deleteByPickedDateKindAndName(currentDate, EventKind.SHIFTS.getIntValue(), eventNames.get(i));
                calendarEventsDao.deleteBypickedDate(currentDate);
            }
        }
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        eventsList.add(eventPosition,
                event);
        eventNames.remove(eventName);
        notifyItemInserted(eventPosition);
    }

    public void onItemMove(int fromPosition, int toPosition) {


        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(eventsList, i, i + 1);
            }

        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(eventsList, i, i - 1);
            }

        }

        notifyItemMoved(fromPosition, toPosition);

    }


    @Override
    public int getItemCount() {
        if (eventsList == null) {
            return 0;
        } else {
            return eventsList.size();
        }
    }

    static class WorkCalendarShiftsViewHolder extends RecyclerView.ViewHolder {
        private TextView shiftName;
        private TextView shiftSchedule;
        private TextView shiftAlarm;
        private LinearLayout shiftLinearLayout;

        WorkCalendarShiftsViewHolder(View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.shiftItem);
            shiftSchedule = itemView.findViewById(R.id.shift_schedule);
            shiftAlarm = itemView.findViewById(R.id.shift_alarm);
            shiftLinearLayout = itemView.findViewById(R.id.shiftTableItemLinearLayout);

        }
    }
}


