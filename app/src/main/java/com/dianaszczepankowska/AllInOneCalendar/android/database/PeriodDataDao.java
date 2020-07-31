package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PeriodDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PeriodData periodData);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PeriodData... periodData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PeriodData periodData);

    @Query("DELETE FROM periodData")
    void deleteAll();

    @Query("DELETE FROM periodData WHERE periodSatrtDate = (SELECT MAX(date(periodSatrtDate)) FROM periodData)")
    void deleteLastPeriod();

    @Query("SELECT * FROM periodData ORDER BY date(periodSatrtDate) DESC Limit 1")
    PeriodData findLastPeriod();

    @Query("SELECT * FROM periodData ORDER BY date(periodSatrtDate) DESC Limit 2")
    List<PeriodData> findPriodBeforeLastOne();

    @Query("SELECT * FROM periodData ORDER BY date(periodSatrtDate) DESC")
    List<PeriodData> findAllPeriodData();

    @Query("SELECT * FROM periodData ORDER BY date(periodSatrtDate) DESC")
    LiveData<List<PeriodData>> getAllPeriodData();
}
