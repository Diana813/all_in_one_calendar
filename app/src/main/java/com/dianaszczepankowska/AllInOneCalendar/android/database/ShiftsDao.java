package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ShiftsDao {

    @Query("SELECT * FROM shift WHERE shift_name = :shiftName LIMIT 1")
    Shift findByShiftName(String shiftName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Shift shift);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Shift... shifts);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Shift shift);

    @Query("DELETE FROM Shift")
    void deleteAll();

    @Query("DELETE FROM shift WHERE shift_name = :shiftName")
    void deleteByShiftName(String shiftName);

    @Query("SELECT * FROM shift ORDER BY position ASC")
    LiveData<List<Shift>> sortByOrder();
}



