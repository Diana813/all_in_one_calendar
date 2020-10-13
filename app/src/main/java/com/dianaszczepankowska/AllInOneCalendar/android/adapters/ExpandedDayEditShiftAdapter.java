package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpandedDayEditShiftAdapter extends RecyclerView.Adapter<ExpandedDayEditShiftAdapter.ExpandedDayShiftsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;
    private int startHour;
    private int startminute;
    private RadioButton lastCheckedRadioButton = null;
    private int selectedItem = -1;


    public ExpandedDayEditShiftAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        ExpandedDayEditShiftAdapter.context = context;

    }

    public static Context getContext() {
        return context;
    }


    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ExpandedDayEditShiftAdapter.ExpandedDayShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.shifts_table_item, parent, false);
        return new ExpandedDayEditShiftAdapter.ExpandedDayShiftsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ExpandedDayEditShiftAdapter.ExpandedDayShiftsViewHolder holder, int position) {

        if (shiftList == null) {
            return;
        }


        final Shift shift = shiftList.get(position);
        if (shift != null) {

            holder.radioButton.setVisibility(View.VISIBLE);
            String shift_start_time = shift.getSchedule();

            holder.shiftName.setText(shift.getShift_name());
            holder.shiftSchedule.setText(shift.getSchedule() + " - " + findEventEndTime(shift_start_time, shift.getShift_length()));
            if (shift.getSchedule().isEmpty()) {
                holder.shiftSchedule.setText("");

            }
            holder.shiftAlarm.setText(shift.getAlarm());

            holder.radioButton.setChecked(position == selectedItem);
        }


    }

    @SuppressLint("DefaultLocale")
    private String findEventEndTime(String schedule, int length) {
        String finish;
        if (schedule.equals("")) {
            finish = "";
        } else {

            String[] parts = schedule.split(":");

            try {
                startHour = Integer.parseInt(parts[0]);
                startminute = Integer.parseInt(parts[1]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();

            }

            int finishHour = startHour + length;
            if (finishHour == 24) {
                finishHour = 0;
            }
            if (finishHour > 24) {
                finishHour = finishHour - 24;
            }

            finish = String.format("%02d:%02d", finishHour, startminute);
        }
        return finish;
    }

    @Override
    public int getItemCount() {
        if (shiftList == null) {
            return 0;
        } else
            return shiftList.size();

    }

    class ExpandedDayShiftsViewHolder extends RecyclerView.ViewHolder {
        private TextView shiftName;
        private TextView shiftSchedule;
        private TextView shiftAlarm;
        private RadioButton radioButton;

        ExpandedDayShiftsViewHolder(View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.shiftItem);
            shiftSchedule = itemView.findViewById(R.id.shift_schedule);
            shiftAlarm = itemView.findViewById(R.id.shift_alarm);
            radioButton = itemView.findViewById(R.id.radioButton);
            View.OnClickListener clickListener = v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            };
            itemView.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);

        }
    }
}


