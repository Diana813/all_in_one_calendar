package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;

import java.time.temporal.ChronoUnit;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.formatPeriodDateWithHyphens;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.LanguageUtils.dayGramma;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.LanguageUtils.monthGramma;

public class PeriodStatisticsAdapter extends RecyclerView.Adapter<PeriodStatisticsAdapter.PeriodStatisticsViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<PeriodData> periodDataList;

    public PeriodStatisticsAdapter(Context context) {
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);
            PeriodStatisticsAdapter.context = context;
        }
    }

    public static Context getContext() {
        return context;
    }


    public void setPeriodDataList(List<PeriodData> periodDataList) {
        this.periodDataList = periodDataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PeriodStatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.statistics_period_data_item, parent, false);
        return new PeriodStatisticsViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull PeriodStatisticsViewHolder holder, int position) {
        if (periodDataList == null) {
            return;
        }
        final PeriodData period = periodDataList.get(position);

        holder.periodStart.setText(formatPeriodDateWithHyphens(period.getPeriodStartDate()));
        holder.periodLength.setText(period.getPeriodLength() + " " + dayGramma(period.getPeriodLength(), context));
        holder.cycleLength.setText(period.getCycleLength() + " " +
                dayGramma(period.getCycleLength(), context));


        if (period == periodDataList.get(getItemCount() - 1)) {
            holder.arrowLayout.setVisibility(View.GONE);
        } else {
            countHowManyMonthsWithoutChange(holder, position, periodDataList);
        }

    }

    @SuppressLint("SetTextI18n")
    private void countHowManyMonthsWithoutChange(PeriodStatisticsViewHolder holder, int position, List<PeriodData> periodDataList) {

        long monthsBetween = ChronoUnit.MONTHS.between(refactorStringIntoDate(periodDataList.get(position + 1).getPeriodStartDate()), refactorStringIntoDate(periodDataList.get(position).getPeriodStartDate()));
        if (monthsBetween >= 1) {
            holder.timeWithoutChanges.setText(monthsBetween + " " + monthGramma((int) monthsBetween, context) + " " + context.getString(R.string.without_changes));
        } else {
            long daysBetween = ChronoUnit.DAYS.between(refactorStringIntoDate(periodDataList.get(position + 1).getPeriodStartDate()), refactorStringIntoDate(periodDataList.get(position).getPeriodStartDate()));
            holder.timeWithoutChanges.setText(daysBetween + " " + dayGramma((int) daysBetween, context));

        }


    }


    @Override
    public int getItemCount() {
        if (periodDataList == null) {
            return 0;
        } else {
            return periodDataList.size();
        }
    }

    static class PeriodStatisticsViewHolder extends RecyclerView.ViewHolder {
        private TextView periodStart;
        private TextView periodLength;
        private TextView cycleLength;
        private TextView timeWithoutChanges;
        private LinearLayout arrowLayout;

        PeriodStatisticsViewHolder(View itemView) {
            super(itemView);
            periodStart = itemView.findViewById(R.id.periodStart);
            periodLength = itemView.findViewById(R.id.periodLength);
            cycleLength = itemView.findViewById(R.id.cycleLength);
            timeWithoutChanges = itemView.findViewById(R.id.timeWithoutChanges);
            arrowLayout = itemView.findViewById(R.id.arrowLayout);

        }
    }
}

