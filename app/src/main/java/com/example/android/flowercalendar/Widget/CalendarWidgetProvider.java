package com.example.android.flowercalendar.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;

public class CalendarWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews remoteView = getView(context);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);

    }

    private static RemoteViews getView(Context context) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget);
        Intent i = new Intent(context, GridViewWidgetService.class);
        views.setRemoteAdapter(R.id.widgetGridView, i);

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.parentView, pendingIntent);

        Intent appIntent = new Intent(context, LoginActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widgetGridView, appPendingIntent);

        return views;
    }


    public static void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        CalendarWidgetUpdateService.startActionUpdateAppWidgets(context, true);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
            CalendarWidgetUpdateService.startActionUpdateAppWidgets(context, true);
        } else super.onReceive(context, intent);
    }


}
