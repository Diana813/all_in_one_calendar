package com.example.android.flowercalendar.database;

import java.io.ByteArrayInputStream;
import java.sql.Blob;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "imagePath")
public class ImagePath {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @ColumnInfo(name = "position")
    private int position;


    public ImagePath(int position, String imagePath) {
        this.position = position;
        this.imagePath = imagePath;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
