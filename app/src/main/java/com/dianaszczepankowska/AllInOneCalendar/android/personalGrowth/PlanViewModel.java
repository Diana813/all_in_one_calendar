package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PlanViewModel extends AndroidViewModel {

    private BigPlanDao bigPlanDao;
    private LiveData<List<BigPlanData>> bigPlanLiveData;
    private LiveData<List<BigPlanData>> bigPlanLiveDataIsChecked;
    private LiveData<List<BigPlanData>> oneYearLiveData;
    private LiveData<List<BigPlanData>> oneYearLiveDataIsChecked;
    private LiveData<List<BigPlanData>> thisMonthLiveData;
    private LiveData<List<BigPlanData>> thisMonthLiveDataIsChecked;
    private LiveData<List<BigPlanData>> oneDayLiveData;
    private LiveData<List<BigPlanData>> oneDayLiveDataIsChecked;


    public PlanViewModel(@NonNull Application application) {
        super(application);
        bigPlanDao = CalendarDatabase.getDatabase(application).bigPlanDao();
        bigPlanLiveData = bigPlanDao.findByAimTime(1);
        bigPlanLiveDataIsChecked = bigPlanDao.findByisChecked(1, 1);
        oneYearLiveData = bigPlanDao.findByAimTime(2);
        oneYearLiveDataIsChecked = bigPlanDao.findByisChecked(2, 1);
        thisMonthLiveData = bigPlanDao.findByAimTime(3);
        thisMonthLiveDataIsChecked = bigPlanDao.findByisChecked(3, 1);
        oneDayLiveData = bigPlanDao.findByAimTime(4);
        oneDayLiveDataIsChecked = bigPlanDao.findByisChecked(4, 1);
    }

    LiveData<List<BigPlanData>> getFiveYearsPlanAimsList() {
        return bigPlanLiveData;
    }

    LiveData<List<BigPlanData>> getFiveYearsPlanAimsListIsChecked() {
        return bigPlanLiveDataIsChecked;
    }

    LiveData<List<BigPlanData>> getOneYearAimsList() {
        return oneYearLiveData;
    }

    LiveData<List<BigPlanData>> getOneYearAimsListIsChecked() {
        return oneYearLiveDataIsChecked;
    }

    LiveData<List<BigPlanData>> getThisMonthAimsList() {
        return thisMonthLiveData;
    }

    LiveData<List<BigPlanData>> getThisMonthAimsListIsChecked() {
        return thisMonthLiveDataIsChecked;
    }

    LiveData<List<BigPlanData>> getOneDayAimsList() {
        return oneDayLiveData;
    }

    LiveData<List<BigPlanData>> getOneDayAimsListIsChecked() {
        return oneDayLiveDataIsChecked;
    }

    public void insert(BigPlanData... aims) {
        bigPlanDao.insert(aims);
    }

    public void insert(BigPlanData aims) {
        bigPlanDao.insert(aims);
    }

    public void update(BigPlanData aims) {
        bigPlanDao.update(aims);
    }

    public void deleteAll() {
        bigPlanDao.deleteAll(1);
    }


}
