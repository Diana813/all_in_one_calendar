package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StatisticsPersonalGrowthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StatisticsPersonalGrowth statisticsPersonalGrowth);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StatisticsPersonalGrowth... statisticsPersonalGrowth);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(StatisticsPersonalGrowth statisticsPersonalGrowth);

    @Query("SELECT * FROM statistics_personal_growth WHERE aim_time = :aimTime")
    LiveData<List<StatisticsPersonalGrowth>> findByAimTime(int aimTime);

    @Query("DELETE FROM statistics_personal_growth")
    void deleteAll();

    @Query("SELECT * FROM statistics_personal_growth ORDER BY aim_time ASC")
    LiveData<List<StatisticsPersonalGrowth>> getAllStatisticsPersonalGrowth();

    @Query("SELECT * FROM statistics_personal_growth WHERE aim_time = :aimTime ORDER BY dateOfAdding = :dateOfAdding DESC limit 1")
    StatisticsPersonalGrowth findLastItem(int aimTime, String dateOfAdding);
}
