package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.CalendarProviderMethods;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DeleteCyclicalEvent;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.StringsAims;
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
    private String id;
    private ArrayList<StringsAims> eventNumbers = new ArrayList<>();
    private String eventContent;
    private String aimTime;
    private DeleteCyclicalEvent deleteCyclicalEvent = new DeleteCyclicalEvent();

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
        View itemView = layoutInflater.inflate(R.layout.plans_item, parent, false);
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
        id = events.getEventId();
        eventNumbers.add(new StringsAims(eventNumber, eventContent, id));
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
                eventsDao.deleteEvents(index, date, content, 1);
            }

        } else if (eventNumbers != null) {

            for (int i = 0; i < eventNumbers.size(); i++) {

                int index = eventNumbers.get(i).getAimNumber();
                String content = eventNumbers.get(i).getAimContent();

                if (aimTime.equals("")) {
                    eventsDao.deleteEvents(index, aimTime, content, 2);
                } else {
                    eventsDao.deleteEvents(index, aimTime, content, 1);
                    deleteCyclicalEvent.deleteCyclicalEventFromPickedDay(eventNumbers.get(i).getAimContent(), eventsDao);
                    CalendarProviderMethods.deleteEvent(context, eventNumbers.get(i).getId());
                }
            }
        }
    }


    public void addToDatabase(ArrayList<StringsAims> stringsAims) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        String date = String.valueOf(LocalDate.now());

        if (stringsAims != null) {

            for (int i = 0; i < stringsAims.size(); i++) {

                int index = stringsAims.get(i).getAimNumber();
                String content = stringsAims.get(i).getAimContent();

                Event event = new Event(index, content, null, null, 0, date, 1, null, null, null);
                eventsDao.insert(event);

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
        eventNumbers.remove(new StringsAims(eventNumber, eventContent, id));
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

