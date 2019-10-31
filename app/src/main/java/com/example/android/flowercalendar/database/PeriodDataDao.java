package com.example.android.flowercalendar.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PeriodDataDao {

    @Query("SELECT * FROM periodData WHERE periodSatrtDate = :periodStartDate")
    PeriodData findByPeriodStartDate(String periodStartDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PeriodData periodData);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PeriodData... periodData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PeriodData periodData);

    @Query("DELETE FROM periodData")
    void deleteAll();

    @Query("SELECT * FROM periodData")
    LiveData<List<PeriodData>> getAllPeriodData();

    @Query("DELETE FROM periodData WHERE periodSatrtDate = :periodStartDate")
    void deleteByPeriodStartDate(String periodStartDate);

    @Query("DELETE FROM periodData WHERE id = (SELECT MAX(ID) FROM periodData)")
    void deleteLastPeriod();

    @Query("SELECT * FROM periodData WHERE id = :id")
    PeriodData findByPeriodById(int id);


    @Query("SELECT * FROM periodData WHERE id = (SELECT MAX(ID) FROM periodData)")
    PeriodData findLastPeriod ();
}
