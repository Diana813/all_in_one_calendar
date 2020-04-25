package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "statistics_personal_growth")
public class StatisticsPersonalGrowth {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "aim_time")
    private int aimTime;
    @ColumnInfo(name = "effectiveness")
    private int effectiveness;
    @ColumnInfo(name = "dateOfAdding")
    private String dateOfAdding;

    public StatisticsPersonalGrowth(int aimTime, int effectiveness, String dateOfAdding) {
        this.aimTime = aimTime;
        this.effectiveness = effectiveness;
        this.dateOfAdding = dateOfAdding;
    }

    public int getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }

    public int getAimTime() {
        return aimTime;
    }

    public void setAimTime(int aimTime) {
        this.aimTime = aimTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateOfAdding() {
        return dateOfAdding;
    }

    public void setDateOfAdding(String dateOfAdding) {
        this.dateOfAdding = dateOfAdding;
    }
}
