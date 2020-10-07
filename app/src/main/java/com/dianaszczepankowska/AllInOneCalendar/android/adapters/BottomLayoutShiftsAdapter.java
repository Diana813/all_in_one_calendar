package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BottomLayoutShiftsAdapter extends RecyclerView.Adapter<BottomLayoutShiftsAdapter.ShiftsViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;
    private Context context;
    private int shiftMarked = -1;

    public BottomLayoutShiftsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = layoutInflater.inflate(R.layout.single_shift_item_bottom_layout, parent, false);
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
            holder.singleShiftItem.setBackgroundResource(R.drawable.shift_selected);
            holder.shift.setTextColor(Color.WHITE);

        } else {
            setDefaultColor(holder);
        }

    }

    private void setDefaultColor(ShiftsViewHolder holder) {
        holder.singleShiftItem.setBackgroundResource(R.drawable.shift_normal);
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
        private FrameLayout singleShiftItem;

        ShiftsViewHolder(View itemView) {
            super(itemView);
            shift = itemView.findViewById(R.id.shift);
            singleShiftItem = itemView.findViewById(R.id.singleShiftItem);
        }
    }
}
