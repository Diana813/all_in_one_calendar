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
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Colors;
import com.example.android.flowercalendar.database.ColorsDao;
import com.example.android.flowercalendar.database.Shift;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        int colorSettings;

        ColorsDao colorsDao = CalendarDatabase.getDatabase(context).colorsDao();
        Colors colorToUpdate = colorsDao.findLastColor1();

  

        if(colorToUpdate == null){
            colorSettings = 0;
        }else{
            colorSettings = colorToUpdate.getColor_number();

        }



        if (colorSettings == 1) {
            //red
            holder.shift.setBackgroundColor(Color.parseColor("#f05545"));
        } else if (colorSettings == 2) {
            //yellow
            holder.shift.setBackgroundColor(Color.parseColor("#fff9c4"));
        } else if (colorSettings == 3) {
            //green
            holder.shift.setBackgroundColor(Color.parseColor("#9cff57"));
        } else if (colorSettings == 4) {
            //blue (#84ffff)
            holder.shift.setBackgroundColor(Color.parseColor("#90caf9"));
        } else if (colorSettings == 5) {
            //violet (#ce93d8)
            holder.shift.setBackgroundColor(Color.parseColor("#e1bee7"));
        } else if (colorSettings == 6) {
            //grey
            holder.shift.setBackgroundColor(Color.parseColor("#BDBDBD"));
        } else {
            //red
            holder.shift.setBackgroundColor(Color.parseColor("#f05545"));
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
