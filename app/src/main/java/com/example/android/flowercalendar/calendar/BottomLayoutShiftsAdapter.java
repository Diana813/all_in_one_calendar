package com.example.android.flowercalendar.calendar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BottomLayoutShiftsAdapter extends RecyclerView.Adapter<BottomLayoutShiftsAdapter.ShiftsViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;
    private Context context;
    private int shiftMarked = -1;

    BottomLayoutShiftsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.single_shift_item, parent, false);
        return new ShiftsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShiftsViewHolder holder, final int position) {

        if (shiftList == null) {
            return;
        }

        final Shift shift = shiftList.get(position);
        holder.shift.setText(shift.getShift_name());


        holder.itemView.setOnClickListener(v -> {
            shiftMarked = position;
            notifyDataSetChanged();
        });
        if (shiftMarked == position) {
            copyShiftNumber(shift);
            holder.shift.setBackgroundResource(R.drawable.frame2);

        } else {
            setDefaultColor(holder);
        }

    }

    private void setDefaultColor(ShiftsViewHolder holder) {
        holder.shift.setBackgroundColor(Color.parseColor("#7CB342"));
    }


    private void copyShiftNumber(Shift shift) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData shiftNumber = ClipData.newPlainText("shiftNumber", shift.getShift_name());
        assert clipboard != null;
        clipboard.setPrimaryClip(shiftNumber);
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
        private TextView shift;

        ShiftsViewHolder(View itemView) {

            super(itemView);
            shift = itemView.findViewById(R.id.shift);
        }
    }
}
