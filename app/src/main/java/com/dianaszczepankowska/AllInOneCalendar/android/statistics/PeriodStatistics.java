package com.dianaszczepankowska.AllInOneCalendar.android.statistics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PeriodStatistics extends Fragment {

    private PeriodStatisticsAdapter periodStatisticsAdapter;
    private PeriodDataDao periodDataDao;
    private TextView periodLength;
    private TextView cycleLength;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        periodStatisticsAdapter = new PeriodStatisticsAdapter(context);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics_period_data, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.ForGirls));

        setHasOptionsMenu(true);

        initData();
        periodDataDao = CalendarDatabase.getDatabase(getContext()).periodDataDao();
        RecyclerView periodsList = rootView.findViewById(R.id.list);
        periodLength = rootView.findViewById(R.id.periodLength);
        cycleLength = rootView.findViewById(R.id.cycleLength);
        periodsList.setAdapter(periodStatisticsAdapter);
        periodsList.setLayoutManager(new LinearLayoutManager(getContext()));
        periodLength.setText(countAveragePeriodLength() + " " + getString(R.string.daysPlural));
        cycleLength.setText(countAverageCycleLength() + " " + getString(R.string.daysPlural));
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        PeriodsViewModel periodsViewModel = new ViewModelProvider(this).get(PeriodsViewModel.class);
        periodsViewModel.getPeriodDataList().observe(getViewLifecycleOwner(), periods -> {
            assert periods != null;
            periodStatisticsAdapter.setPeriodDataList(periods);

        });
    }

    private int countAveragePeriodLength() {
        double totalPeriodLength = 0;
        List<PeriodData> periodsList = periodDataDao.findAllPeriodData();
        assert periodsList != null;
        for (PeriodData period : periodsList) {
            totalPeriodLength += period.getPeriodLength();
        }

        if (periodsList.size() == 0) {
            return 0;
        }
        return (int) Math.round(totalPeriodLength / periodsList.size());
    }

    private int countAverageCycleLength() {
        double totalCycleLength = 0;
        List<PeriodData> periodsList = periodDataDao.findAllPeriodData();
        assert periodsList != null;
        for (PeriodData period : periodsList) {
            totalCycleLength += period.getCycleLength();
        }

        if (periodsList.size() == 0) {
            return 0;
        }
        return (int) Math.round(totalCycleLength / periodsList.size());
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.delete_all));
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deleteAllPeriodData());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllPeriodData() {
        PeriodDataDao dao = CalendarDatabase.getDatabase(getContext()).periodDataDao();
        dao.deleteAll();
        periodLength.setText("");
        cycleLength.setText("");
    }

}
