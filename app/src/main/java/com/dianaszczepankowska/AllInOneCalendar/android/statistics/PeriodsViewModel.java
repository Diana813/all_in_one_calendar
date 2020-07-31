package com.dianaszczepankowska.AllInOneCalendar.android.statistics;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PeriodsViewModel extends AndroidViewModel {
    private PeriodDataDao periodDataDao;
    private LiveData<List<PeriodData>> periodsLiveDataList;


    public PeriodsViewModel(@NonNull Application application) {
        super(application);

        periodDataDao = CalendarDatabase.getDatabase(application).periodDataDao();
        periodsLiveDataList = periodDataDao.getAllPeriodData();
    }

    public LiveData<List<PeriodData>> getPeriodDataList() {
        return periodsLiveDataList;
    }


    public void insert(PeriodData... periods) {
        periodDataDao.insert(periods);
    }

    public void insert(PeriodData period) {
        periodDataDao.insert(period);
    }

    public void update(PeriodData period) {
        periodDataDao.update(period);
    }

    public void deleteAll() {
        periodDataDao.deleteAll();
    }

}
