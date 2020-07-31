package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.EventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.StringsAims;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BigPlanAdapter extends RecyclerView.Adapter<BigPlanAdapter.BigPlanViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<BigPlanData> aimsList;
    private BigPlanData bigPlanData;
    private int pos;
    private ArrayList<StringsAims> aimStrings = new ArrayList<>();
    private int aimTime;
    private ArrayList<StringsAims> toDoListStrings = new ArrayList<>();
    private int currentString;
    private String aimToDelete;
    private Plan plan = new Plan();
    private String date;


    public BigPlanAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        BigPlanAdapter.context = context;
    }


    public static Context getContext() {
        return context;
    }


    void setAimsList(List<BigPlanData> aimsList) {
        this.aimsList = aimsList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BigPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.plans_item, parent, false);
        return new BigPlanViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BigPlanViewHolder holder, int position) {
        if (aimsList == null) {
            return;
        }


        final BigPlanData bigPlanData = aimsList.get(position);
        aimTime = bigPlanData.getAimTime();
        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();
        date = bigPlanData.getStartDate();


        holder.aimNumber.setText(Integer.parseInt(bigPlanData.getAimIndex()) + 1 + ".");
        holder.aimContents.setText(bigPlanData.getAimContents());
        holder.checkBox.setVisibility(View.VISIBLE);
        //prevent situations when checkbox is checked where it shouldn't
        holder.checkBox.setOnCheckedChangeListener(null);
        if (bigPlanData.getIsChecked() == 0) {
            holder.checkBox.setChecked(false);
        } else if (bigPlanData.getIsChecked() == 1) {
            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                bigPlanData.setIsChecked(1);
                bigPlanDao.update(bigPlanData);
            } else {

                bigPlanData.setIsChecked(0);
                bigPlanDao.update(bigPlanData);
            }
        });
    }


    public void deleteItem(int position) {

        bigPlanData = aimsList.get(position);
        pos = position;
        int aimIndex = Integer.parseInt(bigPlanData.getAimIndex());
        String aimContent = bigPlanData.getAimContents();
        aimStrings.add(new StringsAims(aimIndex, aimContent));
        aimsList.remove(position);
        deleteFromDatabase();
        notifyItemRemoved(position);
        showUndoSnackbar();
        findDeletedAimPositionInToDoList(aimContent);
        toDoListStrings.add(new StringsAims(currentString, aimToDelete));
    }


    private void findDeletedAimPositionInToDoList(String toDelete) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        Event eventToDelete = eventsDao.findByEventName(toDelete);
        if (eventToDelete != null) {
            currentString = eventToDelete.getPosition();
            aimToDelete = eventToDelete.getEvent_name();
        }

    }


    public void deleteFromDatabase() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        if (aimStrings != null) {
            for (int i = 0; i < aimStrings.size(); i++) {
                int aimIndex = aimStrings.get(i).getAimNumber();
                String aimContent = aimStrings.get(i).getAimContent();
                bigPlanDao.deleteItemFromPlans(aimIndex, aimTime, aimContent);
            }
        }

        EventsListAdapter eventsListAdapter = new EventsListAdapter(EventsListAdapter.getContext());
        eventsListAdapter.deleteFromDatabase(toDoListStrings);
    }

    public void addToDatabase() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        if (aimStrings != null) {
            for (int i = 0; i < aimStrings.size(); i++) {
                int aimIndex = aimStrings.get(i).getAimNumber();
                String aimContent = aimStrings.get(i).getAimContent();
                BigPlanData aim = new BigPlanData(aimTime, String.valueOf(aimIndex), aimContent, 0, date);
                bigPlanDao.insert(aim);
            }
        }

        EventsListAdapter eventsListAdapter = new EventsListAdapter(EventsListAdapter.getContext());
        eventsListAdapter.addToDatabase(toDoListStrings);
    }


    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.AimDeletedSnackBar,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }


    private void undoDelete() {
        aimsList.add(pos,
                bigPlanData);
        addToDatabase();
        aimStrings.remove(aimStrings.size() - 1);
        toDoListStrings.remove(toDoListStrings.size() - 1);
        notifyItemInserted(pos);
    }


    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(aimsList, i, i + 1);
            }

        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(aimsList, i, i - 1);
            }

        }

        notifyItemMoved(fromPosition, toPosition);
    }


    public void setAimIndexInDB() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        for (BigPlanData bigPlanData : aimsList) {
            bigPlanData.setAimIndex(String.valueOf(aimsList.indexOf(bigPlanData)));
            bigPlanDao.update(bigPlanData);
        }
    }


    @Override
    public int getItemCount() {
        if (aimsList == null) {
            return 0;
        } else {
            return aimsList.size();
        }
    }


    static class BigPlanViewHolder extends RecyclerView.ViewHolder {
        private TextView aimNumber;
        private TextView aimContents;
        private CheckBox checkBox;

        BigPlanViewHolder(View itemView) {
            super(itemView);
            aimNumber = itemView.findViewById(R.id.number);
            aimContents = itemView.findViewById(R.id.contents);
            checkBox = itemView.findViewById(R.id.done);
        }
    }
}
