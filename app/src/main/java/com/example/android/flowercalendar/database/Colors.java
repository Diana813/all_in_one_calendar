package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "colors")
public class Colors {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "color_number")
    private int color_number;

    public Colors(int id,int color_number) {
        this.id = id;
        this.color_number = color_number;
    }

    public int getId() {
        return id;
    }


    public int getColor_number() {
        return color_number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor_number(int color_number) {
        this.color_number = color_number;
    }
}
