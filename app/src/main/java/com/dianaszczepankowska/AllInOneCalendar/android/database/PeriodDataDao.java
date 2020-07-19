package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

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

    @Query("DELETE FROM periodData WHERE id = (SELECT MAX(ID) FROM periodData)")
    void deleteLastPeriod();

    @Query("SELECT * FROM periodData WHERE id = (SELECT MAX(ID) FROM periodData)")
    PeriodData findLastPeriod();

    @Query("SELECT * FROM periodData")
    List<PeriodData> findAllPeriodData();
}
