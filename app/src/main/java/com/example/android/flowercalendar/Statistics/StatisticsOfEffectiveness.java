package com.example.android.flowercalendar.Statistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.StatisticsPersonalGrowthDao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class StatisticsOfEffectiveness extends AppCompatActivity {

    private TextView fiveYears;
    private TextView oneYear;
    private TextView month;
    private TextView day;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_personal_growth);

      /*  StatisticsPersonalGrowthDao statisticsPersonalGrowthDao = CalendarDatabase.getDatabase(getBaseContext()).statisticsPersonalGrowthDao();

        statisticsPersonalGrowthDao.deleteAll();*/

        findViews();
        setSupportActionBar(toolbar);
        initData();
    }

    private void findViews() {
        fiveYears = findViewById(R.id.fiveYears);
        oneYear = findViewById(R.id.oneYear);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
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

                    fiveYears.setText("Your average effectiveness for 5 years plan is: " + averageEffectiveness + "%");
                } else {
                    fiveYears.setText("Your average effectiveness for 5 years plan is: " + "No data available");
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
                    oneYear.setText("Your average effectiveness for 1 year plan is: " + averageEffectiveness + "%");
                } else {
                    oneYear.setText("Your average effectiveness for 1 year plan is: " + "No data available");
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
                    month.setText("Your average effectiveness every month is: " + averageEffectiveness + "%");

                } else {
                    month.setText("Your average effectiveness every month is: " + "No data available");
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
                    day.setText("Your average effectiveness every day is: " + averageEffectiveness + "%");
                } else {
                    day.setText("Your average effectiveness every day is: " + "No data available");
                }

            }
        });
    }
}