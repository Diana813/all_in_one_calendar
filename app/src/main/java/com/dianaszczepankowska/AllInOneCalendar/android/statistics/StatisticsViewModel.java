package com.dianaszczepankowska.AllInOneCalendar.android.statistics;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.StatisticsPersonalGrowth;
import com.dianaszczepankowska.AllInOneCalendar.android.database.StatisticsPersonalGrowthDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StatisticsViewModel extends AndroidViewModel {

    private StatisticsPersonalGrowthDao statisticDao;
    private LiveData<List<StatisticsPersonalGrowth>> statisticsLiveData;
    private LiveData<List<StatisticsPersonalGrowth>> statisticsLiveDataFiveYears;
    private LiveData<List<StatisticsPersonalGrowth>> statisticsLiveDataOneYear;
    private LiveData<List<StatisticsPersonalGrowth>> statisticsLiveDataMonth;
    private LiveData<List<StatisticsPersonalGrowth>> statisticsLiveDataDay;

    public StatisticsViewModel(@NonNull Application application) {

        super(application);
        statisticDao = CalendarDatabase.getDatabase(application).statisticsPersonalGrowthDao();
        statisticsLiveData = statisticDao.getAllStatisticsPersonalGrowth();
        statisticsLiveDataFiveYears = statisticDao.findByAimTime(1);
        statisticsLiveDataOneYear = statisticDao.findByAimTime(2);
        statisticsLiveDataMonth = statisticDao.findByAimTime(3);
        statisticsLiveDataDay = statisticDao.findByAimTime(4);
    }

    public LiveData<List<StatisticsPersonalGrowth>> getStatisticsList() {
        return statisticsLiveData;
    }

    LiveData<List<StatisticsPersonalGrowth>> getStatisticsListFiveYears() {
        return statisticsLiveDataFiveYears;
    }

    LiveData<List<StatisticsPersonalGrowth>> getStatisticsListOneYear() {
        return statisticsLiveDataOneYear;
    }

    LiveData<List<StatisticsPersonalGrowth>> getStatisticsListMonth() {
        return statisticsLiveDataMonth;
    }

    LiveData<List<StatisticsPersonalGrowth>> getStatisticsListDay() {
        return statisticsLiveDataDay;
    }

    public void insert(StatisticsPersonalGrowth... statisticsPersonalGrowth) {
        statisticDao.insert(statisticsPersonalGrowth);
    }

    public void insert(StatisticsPersonalGrowth statisticsPersonalGrowth) {
        statisticDao.insert(statisticsPersonalGrowth);
    }

    public void update(StatisticsPersonalGrowth statisticsPersonalGrowth) {
        statisticDao.update(statisticsPersonalGrowth);
    }

    public void deleteAll() {
        statisticDao.deleteAll();
    }

}
