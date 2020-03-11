package com.example.android.flowercalendar.Events.CyclicalEvents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.OneTimeEvents;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class CyclicalEventsListAdapter extends RecyclerView.Adapter<CyclicalEventsListAdapter.EventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private int startHour;
    private int startminute;
    private Event event;
    private int eventPosition;
    private String eventName;
    private ArrayList<String> eventsNames = new ArrayList<>();
    private String pickedDate;
    private String shiftNumber;
    private String shiftFinish;
    private String shiftStart;


    CyclicalEventsListAdapter(Context requireNonNull, Context context) {
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
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.event_single_item, parent, false);
        return new EventsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        if (eventsList == null) {
            return;
        }

        final Event event = eventsList.get(position);
        pickedDate = eventsList.get(position).getPickedDay();

        if (event != null) {

            String event_start_time = event.getSchedule();

           /* String shift_finish;
            if (event_start_time.equals("")) {
                shift_finish = "";
            } else {

                String[] parts = event_start_time.split(":");

                try {
                    startHour = Integer.parseInt(parts[0]);
                    startminute = Integer.parseInt(parts[1]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();

                }

                int finishHour = startHour + event.getEvent_length();
                if (finishHour == 24) {
                    finishHour = 0;
                }
                if (finishHour > 24) {
                    finishHour = finishHour - 24;
                }

                shift_finish = String.format("%02d:%02d", finishHour, startminute);
            }
*/
           /* holder.eventName.setText(event.getEvent_name());
            holder.eventSchedule.setText(event.getSchedule() + " - " + shift_finish);*/
            if (event.getSchedule().isEmpty()) {
                holder.eventSchedule.setText("");

            }
            // holder.eventAlarm.setText(event.getAlarm());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment oneTimeEvents = OneTimeEvents.newInstance(event.getId(), event.getEvent_name(),
                            event.getSchedule(), event.getAlarm(), event.getEvent_length(), event.getPickedDay());
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, oneTimeEvents).addToBackStack(null).commit();
                }
            });
        }


    }

    public void deleteItem(int position) {

        event = eventsList.get(position);
        eventPosition = position;
        eventName = event.getEvent_name();
        eventsNames.add(eventName);
        eventsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    private void setBackgroundColorItem(View listOfItems, EventsViewHolder holder) {
        ColorsDao colorsDao = getDatabase(context).colorsDao();
        Colors colorToUpdate = colorsDao.findLastColor1();

        if (colorToUpdate == null) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightRed));
        } else if (colorToUpdate.getColor_number() == 1) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightRed));
        } else if (colorToUpdate.getColor_number() == 2) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightYellow));
            holder.eventName.setTextColor(getContext().getResources().getColor(R.color.grey));
            holder.eventSchedule.setTextColor(getContext().getResources().getColor(R.color.grey));
        } else if (colorToUpdate.getColor_number() == 3) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightGreen));
        } else if (colorToUpdate.getColor_number() == 4) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightBlue));
        } else if (colorToUpdate.getColor_number() == 5) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightViolet));
        } else if (colorToUpdate.getColor_number() == 6) {
            listOfItems.setBackgroundColor(getContext().getResources()
                    .getColor(R.color.lightGrey));
        }
    }

    void deleteFromDatabase() {

        EventsDao eventsDao = getDatabase(context).eventsDao();

        if (eventsNames != null) {
            for (int i = 0; i < eventsNames.size(); i++) {

                eventsDao.deleteByEventName(eventsNames.get(i));

            }
        }
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.snack_bar_text,
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
                event);
        eventsNames.remove(eventName);
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
        EventsDao eventsDao = getDatabase(context).eventsDao();
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

    static class EventsViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView eventSchedule;
        private TextView eventAlarm;
        // private LinearLayout listOfItems;


        EventsViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.contents);
            eventSchedule = itemView.findViewById(R.id.number);
            //eventAlarm = itemView.findViewById(R.id.event_alarm);
            //listOfItems = itemView.findViewById(R.id.list_of_items);

        }
    }

}

