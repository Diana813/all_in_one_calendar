package com.example.android.flowercalendar.PersonalGrowth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.StringsAims;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.CalendarEvents;
import com.example.android.flowercalendar.database.CalendarEventsDao;
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
    private String aimIndex;
    private int pos;
    private ArrayList<StringsAims> aimStrings = new ArrayList<>();
    private int aimTime;
    private String aimContent;


    public BigPlanAdapter(Context requireNonNull, Context context) {
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

        holder.aimNumber.setText((position + 1) + ".");
        holder.aimContents.setText(bigPlanData.getAimContents());

    }

    public void deleteItem(int position) {

        bigPlanData = aimsList.get(position);
        pos = position;
        aimIndex = bigPlanData.getAimIndex();
        aimContent = bigPlanData.getAimContents();
        aimStrings.add(new StringsAims(aimIndex, aimContent));
        aimsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    public void deleteFromDatabase() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        if (aimStrings != null) {
            for (int i = 0; i < aimStrings.size(); i++) {
                String aimIndex = aimStrings.get(i).getAimNumber();
                String aimContent = aimStrings.get(i).getAimContent();
                bigPlanDao.deleteItemFromPlans(aimIndex, aimTime, aimContent);
            }
        }
    }

    private void showUndoSnackbar() {

        Snackbar snackbar = Snackbar.make((((Activity) context).findViewById(android.R.id.content)), R.string.AimDeletedSnackBar,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        aimsList.add(pos,
                bigPlanData);
        aimStrings.remove(new StringsAims(aimIndex, aimContent));
        aimStrings = new ArrayList<>();
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

            bigPlanData.setAimIndex(aimsList.indexOf(bigPlanData) + ".");
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

        BigPlanViewHolder(View itemView) {
            super(itemView);
            aimNumber = itemView.findViewById(R.id.number);
            aimContents = itemView.findViewById(R.id.contents);
        }
    }
}
