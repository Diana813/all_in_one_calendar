package com.example.android.flowercalendar.Shifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;

    public ShiftsAdapter(Context requireNonNull, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ShiftsAdapter.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.table_item, parent, false);
        return new ShiftsAdapter.ShiftsViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ShiftsAdapter.ShiftsViewHolder holder, int position) {
        if (shiftList == null) {
            return;
        }

        final Shift shift = shiftList.get(position);
        if (shift != null) {
            holder.shiftName.setText(shift.getShift_name());
            holder.shiftSchedule.setText(shift.getSchedule());
            holder.shiftAlarm.setText(shift.getAlarm());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment editorFragment = ShiftsEditor.newInstance(shift.getId(), shift.getShift_name(),
                            shift.getSchedule(), shift.getAlarm());
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, editorFragment).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (shiftList == null) {
            return 0;
        } else {
            return shiftList.size();
        }
    }

    static class ShiftsViewHolder extends RecyclerView.ViewHolder {
        private TextView shiftName;
        private TextView shiftSchedule;
        private TextView shiftAlarm;

        public ShiftsViewHolder(View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.shift);
            shiftSchedule = itemView.findViewById(R.id.shift_schedule);
            shiftAlarm = itemView.findViewById(R.id.shift_alarm);

        }
    }


}

