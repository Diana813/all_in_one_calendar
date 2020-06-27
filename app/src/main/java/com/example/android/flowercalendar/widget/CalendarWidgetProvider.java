package com.example.android.flowercalendar.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.flowercalendar.LoginActivity;
import com.example.android.flowercalendar.R;

import java.util.Objects;

public class CalendarWidgetProvider extends AppWidgetProvider {


    public static RemoteViews getView(Context context) {

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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews remoteView = getView(context);
            WidgetData.widgetData(context);
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetGridView);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = getView(context);

        if (Objects.equals(intent.getAction(), "UPDATE_WIDGET_AT_MIDNIGHT")) {
            appWidgetManager.updateAppWidget(
                    intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);

            appWidgetManager.notifyAppWidgetViewDataChanged(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), R.id.widgetGridView);

        } else if (Objects.equals(intent.getAction(), "UPDATE_WIDGET_IF_DATA_CHANGED")) {
            appWidgetManager.updateAppWidget(
                    intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);

            appWidgetManager.notifyAppWidgetViewDataChanged(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), R.id.widgetGridView);
        }

    }


}
