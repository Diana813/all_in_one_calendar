package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Shifts.ShiftsAdapter;
import com.example.android.flowercalendar.Shifts.ShiftsEditor;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class CyclicalEventsListAdapter extends RecyclerView.Adapter<CyclicalEventsListAdapter.CyclicalEventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private Event event;
    private int eventPosition;
    private String eventName;
    private ArrayList<String> eventNames = new ArrayList<>();


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
        View itemView = layoutInflater.inflate(R.layout.event_single_item, parent, false);
        return new CyclicalEventsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull CyclicalEventsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }

        final Event event = eventsList.get(position);

        if (event != null) {

            holder.number.setText(position + 1 + ".");
            holder.contents.setText(event.getEvent_name());
            holder.nextEventPlanned.setText("0");
            holder.nextEventPlanned.setVisibility(View.VISIBLE);

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

        if (eventNames != null) {
            for (int i = 0; i < eventNames.size(); i++) {

                eventsDao.deleteByEventName(eventNames.get(i));

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

    void setIndexInDatabase() {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        for (Event event : eventsList) {
            event.setPosition(String.valueOf(eventsList.indexOf(event)));
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

    static class CyclicalEventsViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView contents;
        private TextView nextEventPlanned;

        CyclicalEventsViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            contents = itemView.findViewById(R.id.contents);
            nextEventPlanned = itemView.findViewById(R.id.nextEventPlanned);

        }
    }
}

