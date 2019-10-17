package com.example.android.flowercalendar.Shifts;

import android.app.Application;

import com.example.android.flowercalendar.database.Shift;
import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.database.ShiftsDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ShiftsViewModel extends AndroidViewModel {
    private ShiftsDao shiftsDao;
    private LiveData<List<Shift>> shiftsLiveData;

    public ShiftsViewModel(@NonNull Application application) {
        super(application);
        shiftsDao = ShiftsDatabase.getDatabase(application).shiftsDao();
        shiftsLiveData = shiftsDao.getAllShifts();
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