package com.example.android.flowercalendar.Events;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.StringsAims;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.FrequentActivitiesViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private Event events;
    private int eventPosition;
    private int eventNumber;
    private ArrayList<StringsAims> eventNumbers = new ArrayList<>();
    private String eventContent;
    private String aimTime;

    public EventsListAdapter(Context context) {
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);
            EventsListAdapter.context = context;
        }
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
    public FrequentActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.event_single_item, parent, false);
        return new FrequentActivitiesViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull FrequentActivitiesViewHolder holder, int position) {
        if (eventsList == null) {
            return;
        }
        final Event event = eventsList.get(position);
        aimTime = event.getPickedDay();

        holder.eventNumber.setText(event.getPosition() + ".");
        holder.eventContents.setText(event.getEvent_name());

    }

    public void deleteItem(int position) {

        events = eventsList.get(position);
        eventPosition = position;
        eventNumber = events.getPosition();
        eventContent = events.getEvent_name();
        eventNumbers.add(new StringsAims(eventNumber, eventContent));
        eventsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    public void deleteFromDatabase(ArrayList<StringsAims> stringsAims) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        String date = String.valueOf(LocalDate.now());

        if (stringsAims != null) {

            for (int i = 0; i < stringsAims.size(); i++) {

                int index = stringsAims.get(i).getAimNumber();
                String content = stringsAims.get(i).getAimContent();

                eventsDao.deleteEvents(index, date, content);

            }

        } else if (eventNumbers != null) {

            for (int i = 0; i < eventNumbers.size(); i++) {

                int index = eventNumbers.get(i).getAimNumber();
                String content = eventNumbers.get(i).getAimContent();

                eventsDao.deleteEvents(index, aimTime, content);

            }
        }
    }


    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.AimDeletedSnackBar,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        eventsList.add(eventPosition,
                events);
        eventNumbers.remove(new StringsAims(eventNumber, eventContent));
        eventNumbers = new ArrayList<>();
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

    public void setIndexInDB() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        if (eventsDao != null) {
            for (Event event : eventsList) {
                event.setPosition(eventsList.indexOf(event) + 1);
                eventsDao.update(event);
            }

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

    static class FrequentActivitiesViewHolder extends RecyclerView.ViewHolder {
        private TextView eventNumber;
        private TextView eventContents;

        FrequentActivitiesViewHolder(View itemView) {
            super(itemView);
            eventNumber = itemView.findViewById(R.id.number);
            eventContents = itemView.findViewById(R.id.contents);

        }
    }
}

