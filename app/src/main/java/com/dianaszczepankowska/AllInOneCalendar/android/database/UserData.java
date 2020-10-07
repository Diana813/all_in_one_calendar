package com.dianaszczepankowska.AllInOneCalendar.android.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_data")
public class UserData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "photo")
    private String photo_filed_id;

    public UserData(String name, String email, String photo_filed_id) {
        this.name = name;
        this.email = email;
        this.photo_filed_id = photo_filed_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_filed_id() {
        return photo_filed_id;
    }

    public void setPhoto_filed_id(String photo_filed_id) {
        this.photo_filed_id = photo_filed_id;
    }
}
