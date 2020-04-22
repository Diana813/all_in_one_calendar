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

    @Query("DELETE FROM big_plan_data WHERE aimContents =:aimContent")
    void deleteByAimContent(String aimContent);

    @Query("DELETE FROM big_plan_data WHERE (aimIndex =:aimIndex AND aimTime = :aimTime AND aimContents = :aimContent)")
    void deleteItemFromPlans(String aimIndex, int aimTime, String aimContent);

    @Query("UPDATE big_plan_data SET aimIndex = :aimIndex")
    void updateAimIndex(String aimIndex);

    @Query("DELETE FROM big_plan_data WHERE aimTime = :aimTime")
    void deleteAll(int aimTime);

    @Insert()
    void insert(BigPlanData bigPlanData);

    @Query("SELECT * FROM big_plan_data WHERE aimTime = :aimTime ORDER BY aimIndex ASC")
    LiveData<List<BigPlanData>> findByAimTime(int aimTime);

    @Query("SELECT * FROM big_plan_data WHERE aimTime = :aimTime AND isChecked = :isChecked ORDER BY aimIndex ASC")
    LiveData<List<BigPlanData>> findByisChecked(int aimTime, int isChecked);

}
