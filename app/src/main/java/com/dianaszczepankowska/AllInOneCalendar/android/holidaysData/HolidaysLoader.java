package com.dianaszczepankowska.AllInOneCalendar.android.holidaysData;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class HolidaysLoader extends AsyncTaskLoader<List<Holiday>> {

    private static final String LOG_TAG = HolidaysLoader.class.getName();

    private String mUrl;


    public HolidaysLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Holiday> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchHolidaysData(mUrl);
    }
}
