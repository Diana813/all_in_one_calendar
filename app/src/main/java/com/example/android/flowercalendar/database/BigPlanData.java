package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "big_plan_data")
public class BigPlanData {

    @PrimaryKey(autoGenerate = true)
    private int position;

    @ColumnInfo(name = "aimIndex")
    private String aimIndex;

    @ColumnInfo(name = "aimTime")
    private int aimTime;

    @ColumnInfo(name = "aimContents")
    private String aimContents;

    @ColumnInfo(name = "isChecked")
    private int isChecked;

    public BigPlanData(int aimTime, String aimIndex, String aimContents, int isChecked) {
        this.aimTime = aimTime;
        this.aimIndex = aimIndex;
        this.aimContents = aimContents;
        this.isChecked = isChecked;
    }


    public String getAimContents() {
        return aimContents;
    }

    public void setAimContents(String aimContents) {
        this.aimContents = aimContents;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAimTime() {
        return aimTime;
    }

    public void setAimTime(int aimTime) {
        this.aimTime = aimTime;
    }

    public String getAimIndex() {
        return aimIndex;
    }

    public void setAimIndex(String aimIndex) {
        this.aimIndex = aimIndex;
    }


    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }
}
