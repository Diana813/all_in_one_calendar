package com.example.android.flowercalendar.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    private Contract (){
    }
    public static final String CONTENT_AUTHORITY = "com.example.android.flowercalendar";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PERIOD_DATA = "period_data";
    public static final String PATH_EVENT_DATA = "event_data";

    public static final class PeriodDataEntry implements BaseColumns {

        public static final String TABLE_NAME = "period_data";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_START_DATE = "period_start_date";
        public static final String COLUMN_PERIOD_LENGHT = "period_lenght";
        public static final String COLUMN_CYCLE_LENGHT = "cycle_lenght";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PERIOD_DATA);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERIOD_DATA;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERIOD_DATA;

    }

    public static final class EventDataEntry implements BaseColumns {

        public static final String TABLE_NAME = "event_data";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_REMINDER = "reminder";
        public static final String COLUMN_END = "end";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENT_DATA);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT_DATA;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT_DATA;

    }
}
