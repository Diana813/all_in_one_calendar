package com.example.android.flowercalendar.events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FreqActAdapterSchedule extends RecyclerView.Adapter<FreqActAdapterSchedule.FreqEventsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Event> eventsList;
    private int freqActMarked = -1;

    FreqActAdapterSchedule(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        FreqActAdapterSchedule.context = context;
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
    public FreqActAdapterSchedule.FreqEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.freq_activ_draw_lay_item_view, parent, false);
        return new FreqActAdapterSchedule.FreqEventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FreqActAdapterSchedule.FreqEventsViewHolder holder, final int position) {


        if (eventsList == null) {
            return;
        }
        final Event event = eventsList.get(position);
        if (event != null) {
            holder.eventName.setText(event.getEvent_name());
        }
        holder.itemView.setOnClickListener(v -> {
            freqActMarked = position;
            Toast toast = Toast.makeText(context, "Copied", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackgroundResource(R.drawable.toast);
            toast.show();
            notifyDataSetChanged();
        });

        if (freqActMarked == position) {
            assert event != null;
            String newEvent = event.getEvent_name();
            copyEvent(newEvent);
            holder.backgroundLayout.setBackgroundResource(R.drawable.gradient);

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

    private void copyEvent(String event) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData eventName = ClipData.newPlainText("shiftNumber", event);
        assert clipboard != null;
        clipboard.setPrimaryClip(eventName);
    }


}

