package com.example.android.flowercalendar.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ColorsDao {

    @Insert()
    void insert(Colors color);

    @Update()
    void update(Colors color);

    @Query("SELECT * FROM colors WHERE color_number = :colorNumber")
    Colors findByColorNumber(int colorNumber);

    @Query("SELECT * FROM colors WHERE id = (SELECT MAX(ID) FROM colors)")
    Colors findLastColor1 ();


    @Query("SELECT * FROM colors WHERE id = (SELECT MAX(ID) FROM colors)")
    LiveData<Colors> findLastColor ();

    @Query("DELETE FROM colors")
    void deleteAll();

    @Query("SELECT * FROM colors")
    LiveData<List<Colors>> getAllColors();

}
