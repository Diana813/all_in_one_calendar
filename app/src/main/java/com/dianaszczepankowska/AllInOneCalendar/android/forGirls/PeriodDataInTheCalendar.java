package com.dianaszczepankowska.AllInOneCalendar.android.forGirls;

import android.content.Context;

import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.PeriodDataDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;

public class PeriodDataInTheCalendar {

    public static List<Periods> findPeriodDataList(Context context) {

        List<Periods> periods = new ArrayList<>();
        PeriodDataDao periodDataDao = getDatabase(context).periodDataDao();
        List<PeriodData> periodDataList = periodDataDao.findAllPeriodData();
        for (PeriodData period : periodDataList) {
            String periodStart = period.getPeriodStartDate();
            String[] parts = periodStart.split(":");

            int periodYear = Integer.parseInt(parts[0]);
            int periodMonth = Integer.parseInt(parts[1]);
            int periodDay = Integer.parseInt(parts[2]);
            LocalDate periodStartDate = LocalDate.of(periodYear, periodMonth, periodDay);
            int periodLenght = periodDataDao.findLastPeriod().getPeriodLength();
            int cycleLenght = periodDataDao.findLastPeriod().getCycleLength();

            periods.add(new Periods(periodStartDate, periodLenght, cycleLenght));
        }
        return periods;
    }
}