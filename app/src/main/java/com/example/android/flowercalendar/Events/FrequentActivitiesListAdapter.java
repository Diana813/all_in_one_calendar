package com.example.android.flowercalendar.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Event;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class FrequentActivitiesListAdapter extends RecyclerView.Adapter<FrequentActivitiesListAdapter.FreqEventsViewHolder> {

    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;

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
    public FreqEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.freq_activ_draw_lay_item_view, parent, false);
        return new FreqEventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FrequentActivitiesListAdapter.FreqEventsViewHolder holder, int position) {

        if (eventsList == null) {
            return;
        }
        final Event event = eventsList.get(position);

        if (event != null) {
            holder.eventName.setText(event.getEvent_name());
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

    static class FreqEventsViewHolder  extends RecyclerView.ViewHolder {

        private TextView eventName;

        FreqEventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
        }
    }
}
