package com.example.android.flowercalendar.statistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.flowercalendar.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class StatisticsOfEffectiveness extends AppCompatActivity {

    private TextView fiveYears;
    private TextView fiveYearsStatistics;
    private TextView oneYear;
    private TextView oneYearStatistics;
    private TextView month;
    private TextView monthStatistics;
    private TextView day;
    private TextView dayStatistics;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_personal_growth);
        findViews();
        setSupportActionBar(toolbar);
        initData();
    }

    private void findViews() {
        fiveYears = findViewById(R.id.fiveYears);
        fiveYearsStatistics = findViewById(R.id.fiveYearsStatistic);
        oneYear = findViewById(R.id.oneYear);
        oneYearStatistics = findViewById(R.id.oneYearStatistic);
        month = findViewById(R.id.month);
        monthStatistics = findViewById(R.id.monthStatistic);
        day = findViewById(R.id.day);
        dayStatistics = findViewById(R.id.dayStatistic);
        toolbar = findViewById(R.id.toolbar);
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        StatisticsViewModel statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        statisticsViewModel.getStatisticsListFiveYears().observe(this, fiveYearsList -> {
            int itemCount;
            if (fiveYearsList != null) {
                itemCount = fiveYearsList.size();

                if (itemCount != 0) {
                    int x = 0;

                    for (int i = 0; i < itemCount; i++) {
                        x = x + fiveYearsList.get(i).getEffectiveness();
                    }
                    int averageEffectiveness = x / itemCount;

                    fiveYears.setText(getString(R.string.fiveYears));
                    fiveYearsStatistics.setText(averageEffectiveness + "%");
                } else {
                    fiveYears.setText(getString(R.string.fiveYears) + " " + getString(R.string.noData));
                }

            }

        });

        statisticsViewModel.getStatisticsListOneYear().observe(this, oneYearList -> {
            int itemCount;
            if (oneYearList != null) {
                itemCount = oneYearList.size();
                if (itemCount != 0) {
                    int x = 0;
                    for (int i = 0; i < itemCount; i++) {
                        x = x + oneYearList.get(i).getEffectiveness();
                    }
                    int averageEffectiveness = x / itemCount;
                    oneYear.setText(getString(R.string.oneYear));
                    oneYearStatistics.setText(averageEffectiveness + "%");
                } else {
                    oneYear.setText(getString(R.string.oneYear) + " " + getString(R.string.noData));
                }
            }
        });

        statisticsViewModel.getStatisticsListMonth().observe(this, monthList -> {
            int itemCount;
            if (monthList != null) {
                itemCount = monthList.size();
                if (itemCount != 0) {
                    int x = 0;
                    for (int i = 0; i < itemCount; i++) {
                        x = x + monthList.get(i).getEffectiveness();
                    }
                    int averageEffectiveness = x / itemCount;
                    month.setText(getString(R.string.oneMonth));
                    monthStatistics.setText(averageEffectiveness + "%");

                } else {
                    month.setText(getString(R.string.oneMonth) + " " + getString(R.string.noData));
                }
            }
        });

        statisticsViewModel.getStatisticsListDay().observe(this, dayList -> {

            int itemCount;
            if (dayList != null) {
                itemCount = dayList.size();
                if (itemCount != 0) {
                    int x = 0;
                    for (int i = 0; i < itemCount; i++) {
                        x = x + dayList.get(i).getEffectiveness();
                    }
                    int averageEffectiveness = x / itemCount;
                    day.setText(getString(R.string.oneDay));
                    dayStatistics.setText(averageEffectiveness + "%");
                } else {
                    day.setText(getString(R.string.oneDay) + " " + getString(R.string.noData));
                }

            }
        });
    }
}