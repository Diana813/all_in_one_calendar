package com.example.android.flowercalendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.events.EventsListAdapter;
import com.example.android.flowercalendar.events.ExpandedDayView.ToDoList;
import com.example.android.flowercalendar.events.FrequentActivities.FrequentActivities;
import com.example.android.flowercalendar.gestures.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.personalGrowth.BigPlanAdapter;
import com.example.android.flowercalendar.widget.CalendarWidgetProvider;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.ImagePath;
import com.example.android.flowercalendar.database.ImagePathDao;
import com.example.android.flowercalendar.database.StatisticsPersonalGrowth;
import com.example.android.flowercalendar.database.StatisticsPersonalGrowthDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.android.flowercalendar.personalGrowth.BigPlanAdapter.getContext;
import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class AppUtils {

    public void setRecyclerViewPersonalGrowth(RecyclerView recyclerView, BigPlanAdapter adapter, Context context) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void setItemTouchHelperPersonalGrowth(BigPlanAdapter adapter, RecyclerView recyclerView) {

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void showDeleteConfirmationDialog(final int i) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> {
            BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
            bigPlanDao.deleteAll(i);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showFillInThisFieldDialog(String fieldString, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(fieldString);
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void hideKeyboard(View view, Context context) {

        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(view).getWindowToken(), 0);
        }
    }

    public void setConfirmButton(ImageButton confirm, final BigPlanAdapter adapter, final TextView aim, final int i, String pickedDay, String frequency, String schedule, int eventKind) {

        confirm.setOnClickListener(v -> {
            saveDataPersonalGrowth(adapter, aim, i);
            adapter.deleteFromDatabase();
            adapter.setAimIndexInDB();
            if (pickedDay != null) {
                saveDataEvents(aim, pickedDay, null, frequency, schedule, eventKind);
            }
            aim.setText("");
        });

    }

    public void setConfirmButtonEvents(ImageButton confirm, final EventsListAdapter adapter, final TextView textView, final String pickedDay, final String newEvent, String frequency, String schedule, int eventKind) {
        confirm.setOnClickListener(v -> {
            saveDataEvents(textView, pickedDay, newEvent, frequency, schedule, eventKind);
            adapter.deleteFromDatabase(null);
            adapter.setIndexInDB();
            textView.setText("");
        });

    }


    private void saveDataPersonalGrowth(BigPlanAdapter bigPlanAdapter, TextView aim, int i) {

        String aimTextString = aim.getText().toString();
        int index = bigPlanAdapter.getItemCount();

        LocalDate now = LocalDate.now();

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i, String.valueOf(index), aimTextString, 0, String.valueOf(now)));

    }

    public void saveDataEvents(TextView plan, String pickedDay, String newEvent, String frequency, String schedule, int eventKind) {

        String eventTextString;
        if (newEvent != null) {
            eventTextString = newEvent;
        } else {
            eventTextString = plan.getText().toString();
        }


        int index;
        if (pickedDay.equals("")) {
            index = FrequentActivities.getFreqActSize();
        } else {
            index = ToDoList.positionOfTheNextEventOnTheList();
        }


        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        if (!schedule.equals("")) {
            Event eventToUpdate = eventsDao.findBySchedule(pickedDay, schedule);

            if (eventToUpdate != null) {
                if (eventToUpdate.getSchedule().equals(schedule) &&
                        !eventToUpdate.getEvent_name().equals(eventTextString)) {
                    eventToUpdate.setEvent_name(eventTextString);
                    eventsDao.update(eventToUpdate);
                }

            }
        }

        Event cyclicalEvent = eventsDao.findByEventNameKindAndPickedDay(eventTextString, pickedDay, 1);
        if (cyclicalEvent != null) {
            if (!cyclicalEvent.getEvent_name().equals(eventTextString) ||
                    !cyclicalEvent.getPickedDay().equals(pickedDay) || cyclicalEvent.getEventKind() != eventKind) {
                cyclicalEvent.setEvent_name(eventTextString);
                cyclicalEvent.setPickedDay(pickedDay);
                cyclicalEvent.setEventKind(eventKind);
                eventsDao.update(cyclicalEvent);
            }
        } else {

            eventsDao.insert(new Event(index, eventTextString, schedule, null, 0, pickedDay, eventKind, frequency, "0"));
        }

    }


    public void displayImageFromDB(ImageView view) {

        ImagePathDao imagePathDao = getDatabase(getContext()).imagePathDao();
        ImagePath imagePath = imagePathDao.findLastImage();
        if (imagePath != null) {
            String aimImagePath = imagePath.getImagePath();
            if (aimImagePath.contains("imageDir")) {
                loadImageFromStorage(aimImagePath, view);
            } else if (aimImagePath.contains("JPEG_")) {
                view.setImageURI(Uri.parse(aimImagePath));
            }
        }
    }


    private void loadImageFromStorage(String path, ImageView view) {

        try {
            File file = new File(path, "aimImage.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            view.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void updateWidget(Context context) {

        Intent intent = new Intent(context, CalendarWidgetProvider.class);
        intent.setAction("UPDATE_WIDGET_IF_DATA_CHANGED");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context, CalendarWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 432, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        long millis = zdt.toInstant().toEpochMilli();
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, millis,
                pendingIntent);

    }

    public static void updateWidgetAtMidnight(Context context) {

        Intent intent = new Intent(context, CalendarWidgetProvider.class);
        intent.setAction("UPDATE_WIDGET_AT_MIDNIGHT");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context, CalendarWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        LocalDate todayMidn = LocalDate.now().plusDays(1);
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime todayMidnight = LocalDateTime.of(todayMidn, midnight);
        ZonedDateTime zdt = todayMidnight.atZone(ZoneId.systemDefault());
        long millis = zdt.toInstant().toEpochMilli();
        alarmManager.setRepeating(AlarmManager.RTC, millis, 86400000,
                pendingIntent);
    }


    public static LocalDate refactorStringIntoDate(String stringDate) {

        LocalDate searchedDate;
        if (stringDate == null) {
            searchedDate = null;
        } else {
            String[] split = stringDate.split("-");
            searchedDate = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }

        return searchedDate;
    }


    public static long dateStringToMilis(String dateString) {

        String[] parts = dateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        LocalDateTime periodStartDate = LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day);
        ZonedDateTime zdt = periodStartDate.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();

    }


    @SuppressLint("DefaultLocale")
    public void eventTimeSettingDialog(TextView textView, Context context) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hour, minute) -> textView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
    }


    public String displayDateInAProperFormat(Calendar calendar) {
        String myFormat = "dd MMMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    @SuppressLint("SimpleDateFormat")
    public String changeSimpleDateFormat(String dateStr) {

        String formattedDate;
        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("dd MMMM, yyyy")
                .toFormatter();

        LocalDate parsedDate = LocalDate.parse(dateStr, f);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("MM/d/yyyy");

        formattedDate = parsedDate.format(f2);
        return formattedDate;
    }


    @SuppressLint("SimpleDateFormat")
    public static LocalDate changeSimpleDateFormatToLocalDate(String dateStr) {

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("dd MMMM, yyyy")
                .toFormatter();

        return LocalDate.parse(dateStr, f);
    }

    public void deleteIfTimeIsOut(int i, Context context) {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();
        bigPlanDao.deleteAll(i);
    }

    public int isTheTimeOut(List<BigPlanData> aims, int i) {

        String startDate = aims.get(0).getStartDate();
        String[] parts = startDate.split("-");
        int isTimeOut = 0;
        if (i == 1 || i == 2) {
            String yearString = parts[0];
            isTimeOut = Integer.parseInt(yearString);
        } else if (i == 3) {
            String monthString = parts[1];
            isTimeOut = Month.of(Integer.parseInt(monthString)).getValue();
        } else if (i == 4) {
            String dayString = parts[2];
            isTimeOut = Integer.parseInt(dayString);
        }
        return isTimeOut;
    }

    @SuppressLint("DefaultLocale")
    public Duration howMuchTimeLeft(LocalDate timeOutDate) {

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        ZonedDateTime timeout = ZonedDateTime.of(timeOutDate, LocalTime.of(0, 0, 0), zoneId);
        return Duration.between(now, timeout);
    }

    public void addEffectivenesToDB(Context context, int i, LocalDate dateOfAddingFirstItem, int newEffectiveness) {

        if (newEffectiveness == -1) {
            return;
        }

        StatisticsPersonalGrowthDao statisticsPersonalGrowthDao = CalendarDatabase.getDatabase(context).statisticsPersonalGrowthDao();
        StatisticsPersonalGrowth currentStatistic = statisticsPersonalGrowthDao.findLastItem(i, String.valueOf(dateOfAddingFirstItem));


        int year = 0;
        int month = 0;
        int day = 0;

        if (currentStatistic != null) {
            String addingDate = currentStatistic.getDateOfAdding();
            String[] parts = addingDate.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        }

        if (i == 1) {
            if (currentStatistic != null) {
                if (year + 5 >= LocalDate.now().getYear()) {
                    currentStatistic.setEffectiveness(newEffectiveness);
                    statisticsPersonalGrowthDao.update(currentStatistic);
                }
            } else {
                statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
            }
        } else if (i == 2) {

            if (currentStatistic != null) {
                if (year == LocalDate.now().getYear()) {
                    currentStatistic.setEffectiveness(newEffectiveness);
                    statisticsPersonalGrowthDao.update(currentStatistic);
                } else {
                    statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
                }
            } else {
                statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
            }

        } else if (i == 3) {

            if (currentStatistic != null) {
                if (month == LocalDate.now().getMonthValue()) {
                    currentStatistic.setEffectiveness(newEffectiveness);
                    statisticsPersonalGrowthDao.update(currentStatistic);
                } else {
                    statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
                }
            } else {
                statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
            }
        } else if (i == 4) {

            if (currentStatistic != null) {
                if (day == LocalDate.now().getDayOfMonth()) {
                    currentStatistic.setEffectiveness(newEffectiveness);
                    statisticsPersonalGrowthDao.update(currentStatistic);
                } else {
                    statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
                }
            } else {
                statisticsPersonalGrowthDao.insert(new StatisticsPersonalGrowth(i, newEffectiveness, String.valueOf(dateOfAddingFirstItem)));
            }
        }

    }

    public static boolean isEqualOrBetweenDates(LocalDate searchedDate, LocalDate firstDate, LocalDate secondDate) {

        return (searchedDate.isAfter(firstDate) || searchedDate.isEqual(firstDate)) && (searchedDate.isBefore(secondDate) || searchedDate.isEqual(secondDate));

    }
}