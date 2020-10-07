package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

import androidx.lifecycle.LiveData;
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

    @Query("DELETE FROM imagePath WHERE imagePath = :imagePath")
    void deleteByPath(String imagePath);

    @Query("SELECT * FROM imagePath WHERE id = (SELECT MAX(ID) FROM imagePath)")
    ImagePath findLastImage();

    @Query("SELECT * FROM imagePath")
    LiveData<List<ImagePath>> findAllImages();
}
