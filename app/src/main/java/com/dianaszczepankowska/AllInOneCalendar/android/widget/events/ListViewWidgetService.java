package com.dianaszczepankowska.AllInOneCalendar.android.widget.events;

import android.content.Context;
import android.content.Intent;
import android.icu.util.UniversalTimeScale;
import android.opengl.Visibility;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.LanguageUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public AppWidgetListView onGetViewFactory(Intent intent) {
        return new AppWidgetListView(this.getApplicationContext(), WidgetData.widgetData(getApplicationContext()));

    }
}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

    private List<Event> dataList;
    private Context context;

    AppWidgetListView(Context applicationContext, List<Event> dataList) {
        this.context = applicationContext;
        this.dataList = dataList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        dataList = WidgetData.widgetData(context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dataList.size();
    }


    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.calendar_widget_item_events);

        if (dataList.get(0).getEvent_name().equals(context.getString(R.string.no_plans))) {
            views.setViewVisibility(R.id.eventHours, View.GONE);
            views.setViewVisibility(R.id.startHour, View.INVISIBLE);
        } else {
            views.setViewVisibility(R.id.eventHours, View.VISIBLE);
            views.setViewVisibility(R.id.startHour, View.VISIBLE);

        }

        String pickedDate = dataList.get(position).getPickedDay();
        LocalDate eventDate = DateUtils.refactorStringIntoDate(pickedDate);
        String dayOfWeek = eventDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String dayOfWeekUpperFirstCase = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();

        String month = eventDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String monthUpperFirstCase = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

        if (eventDate.isEqual(LocalDate.now())) {
            views.setTextViewText(R.id.dayOfAWeek, "");
            views.setTextViewText(R.id.dayOfAMonth, context.getString(R.string.today));
            views.setTextViewTextSize(R.id.dayOfAMonth, TypedValue.COMPLEX_UNIT_SP, 18);
            views.setTextViewText(R.id.month, "");
        } else if (eventDate.isEqual(LocalDate.now().plusDays(1))) {
            views.setTextViewText(R.id.dayOfAWeek, "");
            views.setTextViewText(R.id.dayOfAMonth, context.getString(R.string.tomorrow));
            views.setTextViewTextSize(R.id.dayOfAMonth, TypedValue.COMPLEX_UNIT_SP, 18);
            views.setTextViewText(R.id.month, "");
        } else {
            views.setTextViewText(R.id.dayOfAWeek, LanguageUtils.dayOfWeekShortName(eventDate, context) + " ");
            views.setTextViewText(R.id.dayOfAMonth, eventDate.getDayOfMonth() + " ");
            views.setTextViewText(R.id.month, monthUpperFirstCase.substring(0, 3));
        }


        if (position % 2 == 0) {
            views.setInt(R.id.startHour, "setBackgroundResource", R.drawable.widget_frame_hours);
        } else {
            views.setInt(R.id.startHour, "setBackgroundResource", R.drawable.widget_frame_hours2);
        }

        if (position > 0) {
            if (dataList.get(position).getPickedDay().equals(dataList.get(position - 1).getPickedDay())) {
                views.setViewVisibility(R.id.dayOfAWeek, View.INVISIBLE);
                views.setViewVisibility(R.id.dayOfAMonth, View.INVISIBLE);
                views.setViewVisibility(R.id.month, View.INVISIBLE);
                views.setViewPadding(R.id.date, 0, 0, 0, 0);

            } else {
                views.setViewVisibility(R.id.dayOfAWeek, View.VISIBLE);
                views.setViewVisibility(R.id.dayOfAMonth, View.VISIBLE);
                views.setViewVisibility(R.id.month, View.VISIBLE);
                views.setViewPadding(R.id.date, 5, 5, 5, 5);
            }
        } else {
            views.setViewVisibility(R.id.dayOfAWeek, View.VISIBLE);
            views.setViewVisibility(R.id.dayOfAMonth, View.VISIBLE);
            views.setViewVisibility(R.id.month, View.VISIBLE);
            views.setViewPadding(R.id.date, 5, 5, 5, 5);
        }
        LocalTime eventStart;
        LocalTime eventEnd;
        if (dataList.get(position).getSchedule() != null && !dataList.get(position).getSchedule().equals("")) {
            eventStart = LocalTime.parse(dataList.get(position).getSchedule());
            eventEnd = eventStart.plusHours(dataList.get(position).getEvent_length());
            views.setTextViewText(R.id.startHour, String.valueOf(dataList.get(position).getSchedule()));
            if (dataList.get(position).getEvent_length() == 0) {
                views.setTextViewText(R.id.eventHours, String.valueOf(eventStart));
            } else {
                views.setTextViewText(R.id.eventHours, eventStart + " - " + eventEnd);
            }
        } else {
            views.setTextViewText(R.id.eventHours, "");
            views.setTextViewText(R.id.startHour, "");
        }

        views.setTextViewText(R.id.eventName, dataList.get(position).getEvent_name());


        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("dayOfWeek", eventDate.getDayOfWeek().name());
        fillInIntent.putExtra("dayOfMonth", String.valueOf(eventDate.getDayOfMonth()));
        fillInIntent.putExtra("month", eventDate.getMonth().name());
        fillInIntent.putExtra("startHour", dataList.get(position).getSchedule());
        fillInIntent.putExtra("eventName", dataList.get(position).getEvent_name());
        fillInIntent.putExtra("eventHours", dataList.get(position).getSchedule());
        views.setOnClickFillInIntent(R.id.singleEvent, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {

        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
