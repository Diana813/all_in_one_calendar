package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "big_plan_data")
public class BigPlanData {

    @PrimaryKey(autoGenerate = true)
    private int position;

    @ColumnInfo(name = "aimNumber")
    private int aimNumber;

    @ColumnInfo(name = "dot")
    private String dot;

    @ColumnInfo(name = "aimTime")
    private int aimTime;

    @ColumnInfo(name = "aimContents")
    private String aimContents;

    public BigPlanData(int aimTime, int aimNumber, String dot, String aimContents) {
        this.aimTime = aimTime;
        this.aimNumber = aimNumber;
        this.dot = dot;
        this.aimContents = aimContents;
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

    public int getAimNumber() {
        return aimNumber;
    }

    public void setAimNumber(int aimNumber) {
        this.aimNumber = aimNumber;
    }

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }
}
