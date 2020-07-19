package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePathDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BigPlanAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BigPlanAdapter.getContext;

public class AppUtils {

    public static void showDeleteConfirmationDialog(final int i) {

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

    public static void hideKeyboard(View view, Context context) {

        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(view).getWindowToken(), 0);
        }
    }

    public static void setConfirmButton(ImageButton confirm, final BigPlanAdapter adapter, final TextView aim, final int i, String pickedDay, String frequency, String schedule, int eventKind) {

        confirm.setOnClickListener(v -> {
            saveDataPersonalGrowth(adapter, aim, i);
            adapter.deleteFromDatabase();
            adapter.setAimIndexInDB();
            if (pickedDay != null) {
                UtilsEvents.saveDataEvents(aim, pickedDay, null, frequency, schedule, eventKind);
            }
            aim.setText("");
        });

    }


    private static void saveDataPersonalGrowth(BigPlanAdapter bigPlanAdapter, TextView aim, int i) {

        String aimTextString = aim.getText().toString();
        int index = bigPlanAdapter.getItemCount();

        LocalDate now = LocalDate.now();

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i, String.valueOf(index), aimTextString, 0, String.valueOf(now)));

    }


    public static void displayImageFromDB(ImageView view) {

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


    private static void loadImageFromStorage(String path, ImageView view) {

        try {
            File file = new File(path, "aimImage.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            view.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
    public static void eventTimeSettingDialog(TextView textView, Context context) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hour, minute) -> textView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
    }


    public static String displayDateInAProperFormat(Calendar calendar) {
        String myFormat = "dd MMMM, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    @SuppressLint("SimpleDateFormat")
    public static String changeSimpleDateFormat(String dateStr) {

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


    public static boolean isEqualOrBetweenDates(LocalDate searchedDate, LocalDate firstDate, LocalDate secondDate) {

        return (searchedDate.isAfter(firstDate) || searchedDate.isEqual(firstDate)) && (searchedDate.isBefore(secondDate) || searchedDate.isEqual(secondDate));

    }
}