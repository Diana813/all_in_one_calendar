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
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.AddWorkFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AlarmUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_OPEN_NOTIFICATION_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.findEventEndTime;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.WorkViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private int startHour;
    private int startminute;
    private Event event;
    private int eventPosition;
    private String eventName;
    private ArrayList<String> eventNames = new ArrayList<>();


    public WorkListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        WorkListAdapter.context = context;

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
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.event_list_item, parent, false);
        return new WorkViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {

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
            holder.itemView.setOnClickListener(v -> {

                Fragment editorFragment = AddWorkFragment.newInstance(event.getId(), event.getEvent_name(),
                        event.getSchedule(), event.getAlarm(), event.getEvent_length());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, editorFragment).addToBackStack(null).commit();
            });
        }


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

        if (eventNames != null) {
            for (int i = 0; i < eventNames.size(); i++) {

                Event eventToDelete = eventsDao.findByEventKindAndName(eventNames.get(i), EventKind.WORK.getIntValue());
                eventsDao.deleteByPickedDateKindAndName(BackgroundActivityExpandedDayView.currentDate, EventKind.WORK.getIntValue(), eventNames.get(i));
                AlarmUtils.deleteAlarmFromAPickedDay(refactorStringIntoDate(currentDate), AlarmUtils.getAlarmHour(eventToDelete), AlarmUtils.getAlarmMinute(eventToDelete), context, ACTION_OPEN_NOTIFICATION_CLASS);
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

    public void setIndexInDatabase() {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        for (Event event : eventsList) {
            event.setPosition(eventsList.indexOf(event));
            eventsDao.update(event);
        }


    }

    @Override
    public int getItemCount() {
        if (eventsList == null) {
            return 0;
        } else {
            return eventsList.size();
        }
    }

    static class WorkViewHolder extends RecyclerView.ViewHolder {
        private TextView shiftName;
        private TextView shiftSchedule;
        private TextView shiftAlarm;
        private LinearLayout shiftLinearLayout;

        WorkViewHolder(View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.shiftItem);
            shiftSchedule = itemView.findViewById(R.id.shift_schedule);
            shiftAlarm = itemView.findViewById(R.id.shift_alarm);
            shiftLinearLayout = itemView.findViewById(R.id.shiftTableItemLinearLayout);

        }
    }
}


