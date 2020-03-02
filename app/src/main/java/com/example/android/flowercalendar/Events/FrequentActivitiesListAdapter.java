package com.example.android.flowercalendar.Events;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FrequentActivitiesListAdapter extends RecyclerView.Adapter<FrequentActivitiesListAdapter.FrequentActivitiesViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private Event events;
    private int eventPosition;
    private int eventNumber;
    private ArrayList<String> eventNumbers = new ArrayList<>();


    FrequentActivitiesListAdapter(Context requireNonNull, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        FrequentActivitiesListAdapter.context = context;

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

        if (event != null) {

            holder.eventNumber.setText(event.getSchedule());
            holder.eventContents.setText(event.getEvent_name());

        }

    }

    public void deleteItem(int position) {

        events = eventsList.get(position);
        eventPosition = position;
        eventNumber = events.getPosition();
        eventNumbers.add(String.valueOf(eventNumber));
        eventsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    void deleteFromDatabase() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        if (eventNumbers != null) {
            for (int i = 0; i < eventNumbers.size(); i++) {

                bigPlanDao.deleteByAimNumber(eventNumbers.get(i));

            }
        }

        setAimNumbersInDB();
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.AimDeletedSnackBar,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        eventsList.add(eventPosition,
                events);
        eventNumbers.remove(eventNumber);
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
        int i = 1;
        for (Event event : eventsList) {
            event.setPosition(eventsList.indexOf(event));
            event.setPosition(i);
            eventsDao.update(event);
            i++;
        }
    }

    private void setAimNumbersInDB() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        int i = 1;
        for (Event event : eventsList) {

            event.setPosition(i);
            eventsDao.update(event);

            i++;
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
            eventNumber = itemView.findViewById(R.id.eventNumber);
            eventContents = itemView.findViewById(R.id.event);
            ImageView editImage = itemView.findViewById(R.id.editImage);
            editImage.setVisibility(View.GONE);
        }
    }
}

