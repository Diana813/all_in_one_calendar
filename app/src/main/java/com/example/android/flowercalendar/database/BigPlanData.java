package com.example.android.flowercalendar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "big_plan_data")
public class BigPlanData {

        @PrimaryKey(autoGenerate = true)
        private int position;

        @ColumnInfo(name = "aimNumber")
        private String aimNumber;

        @ColumnInfo(name = "aimContents")
        private String aimContents;

        public BigPlanData(String aimNumber, String aimContents) {
            this.aimNumber = aimNumber;
            this.aimContents = aimContents;
        }



    public String getAimNumber() {
        return aimNumber;
    }

    public void setAimNumber(String aimNumber) {
        this.aimNumber = aimNumber;
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
}
