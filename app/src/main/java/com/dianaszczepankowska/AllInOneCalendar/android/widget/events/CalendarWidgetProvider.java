package com.dianaszczepankowska.AllInOneCalendar.android.widget.events;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dianaszczepankowska.AllInOneCalendar.android.LoginActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.time.LocalDate;
import java.util.Objects;

public class CalendarWidgetProvider extends AppWidgetProvider {

    static int dayNumber;


    public static RemoteViews getView(Context context) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget_events);

        Intent i = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.widgetListView, i);

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.parentViewEvents, pendingIntent);

        Intent appIntent = new Intent(context, LoginActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widgetListView, appPendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            RemoteViews remoteView = getView(context);
            WidgetData.widgetData(context);
            appWidgetManager.updateAppWidget(appWidgetId, remoteView);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = getView(context);

        if (Objects.equals(intent.getAction(), "UPDATE_WIDGET_AT_MIDNIGHT_EVENTS")) {
            appWidgetManager.updateAppWidget(
                    intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);

            appWidgetManager.notifyAppWidgetViewDataChanged(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), R.id.widgetListView);

        } else if (Objects.equals(intent.getAction(), "UPDATE_WIDGET_IF_DATA_CHANGED_EVENTS")) {
            appWidgetManager.updateAppWidget(
                    intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), views);

            appWidgetManager.notifyAppWidgetViewDataChanged(intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), R.id.widgetListView);
        }

    }


}

