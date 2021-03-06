package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.DailyScheduleEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.ToDoList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FrequentActivitiesDrawerListAdapter extends RecyclerView.Adapter<FrequentActivitiesDrawerListAdapter.FreqEventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private int freqActMarked = -1;
    private ToDoList toDoList = new ToDoList();
    private DailyScheduleEvents dailyScheduleEvents = new DailyScheduleEvents();

    public FrequentActivitiesDrawerListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        FrequentActivitiesDrawerListAdapter.context = context;
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
    public FreqEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.freq_activ_draw_lay_item_view, parent, false);
        return new FreqEventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FrequentActivitiesDrawerListAdapter.FreqEventsViewHolder holder, final int position) {


        if (eventsList == null) {
            return;
        }
        final Event event = eventsList.get(position);
        if (event != null) {
            holder.eventName.setText(event.getEvent_name());
        }
        holder.itemView.setOnClickListener(v -> {
            freqActMarked = position;
            notifyDataSetChanged();
        });

        if (freqActMarked == position) {
            assert event != null;
            String newEvent = event.getEvent_name();
            String pickedDay = CalendarFragment.pickedDay;
            toDoList.saveEvent(newEvent, pickedDay);
            holder.backgroundLayout.setBackgroundResource(R.color.colorAccent);
            freqActMarked = -2;

        } else {
            holder.backgroundLayout.setBackgroundColor(Color.WHITE);
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

    static class FreqEventsViewHolder extends RecyclerView.ViewHolder {

        private TextView eventName;
        private LinearLayout backgroundLayout;

        FreqEventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.numberOfEvents);
            backgroundLayout = itemView.findViewById(R.id.singleFreqActItem);
        }
    }


}
