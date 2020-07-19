package com.dianaszczepankowska.AllInOneCalendar.android.shifts;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ShiftsViewModel extends AndroidViewModel {
    private ShiftsDao shiftsDao;
    private LiveData<List<Shift>> shiftsLiveData;

    public ShiftsViewModel(@NonNull Application application) {
        super(application);
        shiftsDao = CalendarDatabase.getDatabase(application).shiftsDao();
        shiftsLiveData = shiftsDao.sortByOrder();
    }

    public LiveData<List<Shift>> getShiftsList() {
        return shiftsLiveData;
    }

    public void insert(Shift... shifts) {
        shiftsDao.insert(shifts);
    }

    public void insert(Shift shift) {
        shiftsDao.insert(shift);
    }

    public void update(Shift shift) {
        shiftsDao.update(shift);
    }

    public void deleteAll() {
        shiftsDao.deleteAll();
    }

}