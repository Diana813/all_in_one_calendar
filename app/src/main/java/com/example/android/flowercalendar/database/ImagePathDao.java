package com.example.android.flowercalendar.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ImagePathDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ImagePath imagePath);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(ImagePath imagePath);

    @Query("DELETE FROM imagePath")
    void deleteAll();

    @Query("SELECT * FROM imagePath WHERE id = (SELECT MAX(ID) FROM imagePath)")
    ImagePath findLastImage ();
}
