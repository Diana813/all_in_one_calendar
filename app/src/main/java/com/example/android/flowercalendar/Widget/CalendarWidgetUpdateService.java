package com.example.android.flowercalendar.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.flowercalendar.R;

import androidx.annotation.Nullable;

public class CalendarWidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_APP_WIDGETS_AT_MIDNIGHT = "com.example.android.flowercalendar.Widget.CalendarWidgetUpdateService.update_app_widget_at_midnight";
    public static final String ACTION_UPDATE_GRID_VIEW = "com.example.android.flowercalendar.Widget.CalendarWidgetUpdateService.update_app_widget_list";

    public CalendarWidgetUpdateService() {
        super("CalendarWidgetUpdateService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_GRID_VIEW.equals(action)) {
                handleActionUpdateGridView(this);
            } else if (ACTION_UPDATE_APP_WIDGETS_AT_MIDNIGHT.equals(action)) {
                handleActionUpdateGridView(this);
            }
        }
    }

    static void handleActionUpdateGridView(Context context) {

        WidgetData.widgetData(context.getApplicationContext());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CalendarWidgetProvider.class));

        CalendarWidgetProvider.updateAllAppWidgets(context, appWidgetManager, appWidgetIds);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetGridView);

    }

    public static void startActionUpdateAppWidgets(Context context, boolean gridView) {
        Intent intent = new Intent(context, CalendarWidgetUpdateService.class);
        if (gridView) {
            intent.setAction(ACTION_UPDATE_GRID_VIEW);
        }
        context.startService(intent);
    }
}
