package com.example.android.flowercalendar.events.CyclicalEvents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.alarm.CyclicalEventsNotifications;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CyclicalEventsListAdapter extends RecyclerView.Adapter<CyclicalEventsListAdapter.CyclicalEventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private Event event;
    private int eventPosition;
    private String eventName;
    private ArrayList<String> eventNames = new ArrayList<>();
    private UpcomingCyclicalEvent upcomingCyclicalEvent = new UpcomingCyclicalEvent();


    CyclicalEventsListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        CyclicalEventsListAdapter.context = context;

    }

    public static Context getContext() {
        return context;
    }


    void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CyclicalEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.plans_item, parent, false);
        return new CyclicalEventsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CyclicalEventsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }

        final Event event = eventsList.get(position);
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (event != null) {


            holder.number.setVisibility(View.GONE);
            holder.contents.setText(event.getEvent_name());
            String[] parts = event.getFrequency().split("-");
            String pickedDaysOfWeek = parts[3];

            if (event.getTerm() == null) {
                event.setTerm("on_and_on");
                eventsDao.update(event);
            }

            LocalDate upcomingEvent = upcomingCyclicalEvent.displayNextEvent(event.getPickedDay(), event.getFrequency(), pickedDaysOfWeek, event.getTerm());


            Event previousEvent;
            try {
                previousEvent = eventsList.get(position - 1);
            } catch (IndexOutOfBoundsException e) {
                previousEvent = null;
            }

            holder.nextEventPlanned.setVisibility(View.VISIBLE);

            if (previousEvent != null &&
                    event.getEvent_name().equals(previousEvent.getEvent_name()) &&
                    (upcomingEvent.isAfter(upcomingCyclicalEvent.displayNextEvent(previousEvent.getPickedDay(), previousEvent.getFrequency(), pickedDaysOfWeek, previousEvent.getTerm())))) {
                holder.contents.setVisibility(View.GONE);
                holder.nextEventPlanned.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }

            LocalDate today = LocalDate.now();

            if (upcomingEvent == null) {
                holder.nextEventPlanned.setText("Finished");

            } else if (String.valueOf(upcomingEvent).equals(String.valueOf(today))) {
                holder.nextEventPlanned.setText("Today");

            } else {
                holder.nextEventPlanned.setText(upcomingEvent.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy")));
            }

            holder.itemView.setOnClickListener(v -> {
                Fragment editorFragment = CyclicalEventsDetails.newInstance(event.getId(), event.getEvent_name(), event.getPickedDay(), event.getFrequency(), event.getSchedule(), 0, event.getEvent_length(), event.getAlarm(), event.getTerm());
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

    void deleteFromDatabase() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        CyclicalEventsNotifications cyclicalEventsNotifications = new CyclicalEventsNotifications();

        if (eventNames != null) {
            for (int i = 0; i < eventNames.size(); i++) {

                Event event = eventsDao.findByEventKindAndName(eventNames.get(i), 0);
                cyclicalEventsNotifications.deleteNotification(event, context.getApplicationContext());

                eventsDao.deleteByEventName(eventNames.get(i));


            }
        }
    }


    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), "Event deleted",
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


    @Override
    public int getItemCount() {
        if (eventsList == null) {
            return 0;
        } else {
            return eventsList.size();
        }
    }


    static class CyclicalEventsViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView contents;
        private TextView nextEventPlanned;
        private View line;

        CyclicalEventsViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            contents = itemView.findViewById(R.id.contents);
            nextEventPlanned = itemView.findViewById(R.id.nextEventPlanned);
            line = itemView.findViewById(R.id.line);

        }
    }

}

