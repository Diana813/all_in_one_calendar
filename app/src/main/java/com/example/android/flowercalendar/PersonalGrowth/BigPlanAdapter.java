package com.example.android.flowercalendar.PersonalGrowth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
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
    private int aimPosition;
    private String aimNumber;
    private ArrayList<String> aimNumbers = new ArrayList<>();


    BigPlanAdapter(Context requireNonNull, Context context) {
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
        View itemView = layoutInflater.inflate(R.layout.big_plan_item, parent, false);
        return new BigPlanViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BigPlanViewHolder holder, int position) {
        if (aimsList == null) {
            return;
        }

        final BigPlanData bigPlanData = aimsList.get(position);

        if (bigPlanData != null) {

            holder.aimNumber.setText(bigPlanData.getAimNumber());
            holder.aimContents.setText(bigPlanData.getAimContents());

        }

    }

    public void deleteItem(int position) {

        bigPlanData = aimsList.get(position);
        aimPosition = position;
        aimNumber = bigPlanData.getAimNumber();
        aimNumbers.add(aimNumber);
        aimsList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();

    }

    void deleteFromDatabase() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();

        if (aimNumbers != null) {
            for (int i = 0; i < aimNumbers.size(); i++) {

                bigPlanDao.deleteByAimNumber(aimNumbers.get(i));

            }
        }

        setAimNumbersInDB();
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
        aimsList.add(aimPosition,
                bigPlanData);
        aimNumbers.remove(aimNumber);
        notifyItemInserted(aimPosition);
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

    void setIndexInDatabase() {
        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();
        int i = 1;
        for (BigPlanData bigPlanData : aimsList) {
            bigPlanData.setPosition(aimsList.indexOf(bigPlanData));
            bigPlanData.setAimNumber(String.valueOf(i));
            bigPlanDao.update(bigPlanData);
            i++;
        }
    }

    private void setAimNumbersInDB() {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();
        int i = 1;
        for (BigPlanData bigPlanData : aimsList) {

            bigPlanData.setAimNumber(String.valueOf(i));
            bigPlanDao.update(bigPlanData);

            i++;
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
            aimNumber = itemView.findViewById(R.id.aimNumber);
            aimContents = itemView.findViewById(R.id.aimContents);
        }
    }
}
