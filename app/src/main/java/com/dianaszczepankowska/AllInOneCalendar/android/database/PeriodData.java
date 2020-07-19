package com.dianaszczepankowska.AllInOneCalendar.android.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "periodData")
public class PeriodData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo (name = "periodSatrtDate")
    private String periodStartDate;
    @ColumnInfo(name = "periodLength")
    private int periodLength;
    @ColumnInfo(name = "cycleLength")
    private int cycleLength;

    public PeriodData(String periodStartDate, int periodLength, int cycleLength) {
        this.periodStartDate = periodStartDate;
        this.periodLength = periodLength;
        this.cycleLength = cycleLength;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public int getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(int periodLength) {
        this.periodLength = periodLength;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
