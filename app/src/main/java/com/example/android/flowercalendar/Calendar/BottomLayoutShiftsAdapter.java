package com.example.android.flowercalendar.Calendar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.android.flowercalendar.database.ColorsDatabase.getDatabase;

public class BottomLayoutShiftsAdapter extends RecyclerView.Adapter<BottomLayoutShiftsAdapter.ShiftsViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;
    private Context context;

    public BottomLayoutShiftsAdapter(Context requireNonNull, Context context) {
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
        final View itemView = layoutInflater.inflate(R.layout.single_shift_item, parent, false);
        return new ShiftsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShiftsViewHolder holder, int position) {

        if (shiftList == null) {
            return;
        }

        final Shift shift = shiftList.get(position);

        holder.shift.setText(shift.getShift_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager)
                        context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData shiftNumber = ClipData.newPlainText("shiftNumber", shift.getShift_name());
                assert clipboard != null;
                clipboard.setPrimaryClip(shiftNumber);
            }
        });

        ColorsDao colorsDao = getDatabase(context).colorsDao();
        Colors colorToUpdate = colorsDao.findLastColor1();

        int colorSettings;

        if(colorToUpdate == null){
            colorSettings = 0;
        }else{
            colorSettings = colorToUpdate.getColor_number();

        }



        if (colorSettings == 1) {
            holder.shift.setBackgroundColor(Color.parseColor("#b71c1c"));
            holder.shift.setTextColor(Color.WHITE);
        } else if (colorSettings == 2) {
            holder.shift.setBackgroundColor(Color.parseColor("#ffeb3b"));
        } else if (colorSettings == 3) {
            holder.shift.setBackgroundColor(Color.parseColor("#64dd17"));
        } else if (colorSettings == 4) {
            holder.shift.setBackgroundColor(Color.parseColor("#00e5ff"));
        } else if (colorSettings == 5) {
            holder.shift.setBackgroundColor(Color.parseColor("#aa00ff"));
            holder.shift.setTextColor(Color.WHITE);
        } else if (colorSettings == 6) {
            holder.shift.setBackgroundColor(Color.parseColor("#757575"));
            holder.shift.setTextColor(Color.WHITE);
        } else {
            holder.shift.setBackgroundColor(Color.parseColor("#b71c1c"));
            holder.shift.setTextColor(Color.parseColor("#000000"));
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
        private TextView shift;

        ShiftsViewHolder(View itemView) {

            super(itemView);
            shift = itemView.findViewById(R.id.shift);
        }
    }
}
