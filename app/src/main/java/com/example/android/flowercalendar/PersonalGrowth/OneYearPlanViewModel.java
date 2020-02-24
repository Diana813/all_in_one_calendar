package com.example.android.flowercalendar.PersonalGrowth;

import android.app.Application;

import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OneYearPlanViewModel extends AndroidViewModel {

    private BigPlanDao bigPlanDao;
    private LiveData<List<BigPlanData>> bigPlanLiveData;

    public OneYearPlanViewModel(@NonNull Application application) {
        super(application);
        bigPlanDao = CalendarDatabase.getDatabase(application).bigPlanDao();
        bigPlanLiveData = bigPlanDao.findByAimTime(2);
    }

    LiveData<List<BigPlanData>> getAimsList() {
        return bigPlanLiveData;
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
        bigPlanDao.deleteAll();
    }

}
