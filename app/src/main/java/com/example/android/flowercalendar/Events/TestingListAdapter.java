package com.example.android.flowercalendar.Events;

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

public class TestingListAdapter extends RecyclerView.Adapter<TestingListAdapter.EventsViewHolder> {

    private static Context context;
    private final LayoutInflater layoutInflater;
    private List<Event> eventsList;

    TestingListAdapter(Context requireNonNull, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        TestingListAdapter.context = context;
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
    public TestingListAdapter.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.single_testing_item, parent, false);
        return new EventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestingListAdapter.EventsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }

        final Event event = eventsList.get(position);



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
