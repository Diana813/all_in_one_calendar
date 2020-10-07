package com.dianaszczepankowska.AllInOneCalendar.android.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserData userData);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserData... userData);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserData userData);

    @Query("SELECT * FROM user_data WHERE name = :name")
    LiveData<List<UserData>> findAllByName(String name);

    @Query("SELECT * FROM user_data WHERE name = :name")
    UserData findByName(String name);

    @Query("SELECT * FROM user_data WHERE email = :email")
    LiveData<List<UserData>> findAllByEmail(String email);

    @Query("SELECT * FROM user_data WHERE email = :email")
    UserData findByEmail(String email);

    @Query("DELETE FROM user_data")
    void deleteAll();

    @Query("SELECT * FROM user_data ORDER BY name ASC")
    List<UserData> getAllUsers();


}
