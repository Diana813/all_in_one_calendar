package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePathDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter.getContext;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.savePlansData;

public class DialogsUtils {

    public static void showDeleteConfirmationDialogAims(final int i) {

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

    public static void showDeleteConfirmationDialogImage(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> {
            ImagePathDao imagePathDao = CalendarDatabase.getDatabase(context).imagePathDao();
            imagePathDao.deleteAll();
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


    @SuppressLint("DefaultLocale")
    public static void eventTimeSettingDialog(TextView textView, Context context) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hour, minute) -> textView.setText(String.format("%02d:%02d", hour, minute)), 0, 0, true);
        timePickerDialog.show();
    }


    public static boolean isEqualOrBetweenDates(LocalDate searchedDate, LocalDate firstDate, LocalDate secondDate) {

        return (searchedDate.isAfter(firstDate) || searchedDate.isEqual(firstDate)) && (searchedDate.isBefore(secondDate) || searchedDate.isEqual(secondDate));

    }


    public static void createEditTextDialog(Context context, final PlansRecyclerViewAdapter adapter, final int i, String pickedDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.task));

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> savePlansData(adapter, input, i, pickedDay));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public static String findShiftFinish(String shiftSchedule, int shiftLenght) {

        if (!shiftSchedule.equals("")) {
            String[] split = shiftSchedule.split(":");
            int shiftStartHour = Integer.parseInt(split[0]);
            int shiftStartMinutes = Integer.parseInt(split[1]);

            return String.valueOf(LocalTime.of(shiftStartHour, shiftStartMinutes).plusHours(shiftLenght));
        }
        return null;
    }
}