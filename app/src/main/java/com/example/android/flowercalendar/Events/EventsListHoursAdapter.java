package com.example.android.flowercalendar.Events;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class EventsListHoursAdapter extends RecyclerView.Adapter<EventsListHoursAdapter.EventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private final LayoutInflater layoutInflater;
    private List<Hours> eventsList;

    EventsListHoursAdapter(Context requireNonNull, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        EventsListHoursAdapter.context = context;
    }

    public static Context getContext() {
        return context;
    }


    void setEventsList(List<Hours> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public EventsListHoursAdapter.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.single_hours_item, parent, false);
        return new EventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsListHoursAdapter.EventsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }

        final Hours hour = eventsList.get(position);
        holder.hour.setText(hour.getHour());

    }

    @Override
    public int getItemCount() {
        if (eventsList == null) {
            return 0;
        } else {
            return eventsList.size();
        }
    }

    public void setIndexInDatabase() {
    }

    public void deleteFromDatabase() {
    }

    public void onItemMove(int adapterPosition, int adapterPosition1) {
    }

    static class EventsViewHolder extends RecyclerView.ViewHolder {
        private TextView hour;
        private CardView event;
        private LinearLayout listOfItems;

        EventsViewHolder(View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            event = itemView.findViewById(R.id.event);
            listOfItems = itemView.findViewById(R.id.list_of_items);

        }
    }

}
