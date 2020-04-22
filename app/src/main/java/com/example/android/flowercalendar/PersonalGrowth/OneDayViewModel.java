package com.example.android.flowercalendar.PersonalGrowth;

import android.app.Application;

import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class OneDayViewModel extends AndroidViewModel {

    private BigPlanDao bigPlanDao;
    private LiveData<List<BigPlanData>> bigPlanLiveData;
    private LiveData<List<BigPlanData>> bigPlanLiveDataIsChecked;

    public OneDayViewModel(@NonNull Application application) {
        super(application);
        bigPlanDao = CalendarDatabase.getDatabase(application).bigPlanDao();
        bigPlanLiveData = bigPlanDao.findByAimTime(4);
        bigPlanLiveDataIsChecked = bigPlanDao.findByisChecked(4, 1);
    }

    LiveData<List<BigPlanData>> getAimsList() {
        return bigPlanLiveData;
    }

    LiveData<List<BigPlanData>> getAimsListIsChecked() {
        return bigPlanLiveDataIsChecked;
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
        bigPlanDao.deleteAll(4);
    }

}
