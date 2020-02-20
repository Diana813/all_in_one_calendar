package com.example.android.flowercalendar.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BigPlanDao {


    @Insert()
    void insert(BigPlanData[] bigPlanData);

    @Update()
    void update(BigPlanData bigPlanData);

    @Query("SELECT * FROM big_plan_data WHERE aimNumber = :aimNumber")
    BigPlanData findByAimNumber(String aimNumber);

    @Query("DELETE FROM big_plan_data WHERE aimNumber =:aimNumber")
    void deleteByAimNumber(String aimNumber);

    @Query("UPDATE big_plan_data SET aimNumber = :aimNumber")
    void updateAimNumber(String aimNumber);

    @Query("DELETE FROM big_plan_data")
    void deleteAll();

    @Query("SELECT * FROM big_plan_data")
    LiveData<List<BigPlanData>> getAllBigPlanData();

    @Insert()
    void insert(BigPlanData bigPlanData);

    @Query("SELECT * FROM big_plan_data ORDER BY position ASC")
    LiveData<List<BigPlanData>> sortByOrder();
}
