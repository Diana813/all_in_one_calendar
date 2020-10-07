package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsEditor;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ShiftListAdapter extends RecyclerView.Adapter<ShiftListAdapter.ShiftsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<Shift> shiftList;
    private int startHour;
    private int startminute;
    private Shift shift;
    private int shiftPosition;
    private String shiftName;
    private ArrayList<String> shiftsNames = new ArrayList<>();


    public ShiftListAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        ShiftListAdapter.context = context;

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
    public ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.shifts_table_item, parent, false);
        return new ShiftsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ShiftsViewHolder holder, int position) {

        if (shiftList == null) {
            return;
        }


        final Shift shift = shiftList.get(position);
        if (shift != null) {

            String shift_start_time = shift.getSchedule();

            holder.shiftName.setText(shift.getShift_name());
            holder.shiftSchedule.setText(shift.getSchedule() + " - " + findEventEndTime(shift_start_time, shift.getShift_length()));
            if (shift.getSchedule().isEmpty()) {
                holder.shiftSchedule.setText("");

            }
            holder.shiftAlarm.setText(shift.getAlarm());
            holder.itemView.setOnClickListener(v -> {
                Fragment editorFragment = ShiftsEditor.newInstance(shift.getId(), shift.getShift_name(),
                        shift.getSchedule(), shift.getAlarm(), shift.getShift_length());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, editorFragment).addToBackStack(null).commit();
            });
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


    public void deleteItem(int position) {

        shift = shiftList.get(position);
        shiftPosition = position;
        shiftName = shift.getShift_name();
        shiftsNames.add(shiftName);
        shiftList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    public void deleteFromDatabase() {

        ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();

        if (shiftsNames != null) {
            for (int i = 0; i < shiftsNames.size(); i++) {

                shiftsDao.deleteByShiftName(shiftsNames.get(i));
            }
        }
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        shiftList.add(shiftPosition,
                shift);
        shiftsNames.remove(shiftName);
        notifyItemInserted(shiftPosition);
    }

    public void onItemMove(int fromPosition, int toPosition) {


        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(shiftList, i, i + 1);
            }

        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(shiftList, i, i - 1);
            }

        }

        notifyItemMoved(fromPosition, toPosition);

    }

    public void setIndexInDatabase() {
        ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
        for (Shift shift : shiftList) {
            shift.setPosition(shiftList.indexOf(shift));
            shiftsDao.update(shift);
        }
    }

    @Override
    public int getItemCount() {
        if (shiftList == null) {
            return 0;
        } else
            return shiftList.size();

    }

    static class ShiftsViewHolder extends RecyclerView.ViewHolder {
        private TextView shiftName;
        private TextView shiftSchedule;
        private TextView shiftAlarm;

        ShiftsViewHolder(View itemView) {
            super(itemView);
            shiftName = itemView.findViewById(R.id.shiftItem);
            shiftSchedule = itemView.findViewById(R.id.shift_schedule);
            shiftAlarm = itemView.findViewById(R.id.shift_alarm);

        }
    }
}

