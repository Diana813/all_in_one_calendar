package com.example.android.flowercalendar;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter;
import com.example.android.flowercalendar.Widget.CalendarWidgetProvider;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;
import com.example.android.flowercalendar.database.ImagePath;
import com.example.android.flowercalendar.database.ImagePathDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter.getContext;
import static com.example.android.flowercalendar.Widget.CalendarWidgetUpdateService.ACTION_UPDATE_GRID_VIEW;
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

    public void hideKeyboard(View view, Context context) {

        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(view).getWindowToken(), 0);
        }
    }

    public void setConfirmButton(ImageButton confirm, final BigPlanAdapter adapter, final TextView aim, final int i, String pickedDay, EventsListAdapter eventsListAdapter) {

        confirm.setOnClickListener(v -> {
            saveDataPersonalGrowth(adapter, aim, i);
            adapter.deleteFromDatabase();
            adapter.setAimIndexInDB();
            saveDataEvents(eventsListAdapter, aim, 1, pickedDay, null);
            aim.setText("");
        });

    }

    public void setConfirmButtonEvents(ImageButton confirm, final EventsListAdapter adapter, final TextView textView, final int i, final String pickedDay, final String newEvent) {
        confirm.setOnClickListener(v -> {
            saveDataEvents(adapter, textView, i, pickedDay, newEvent);
            adapter.deleteFromDatabase(null);
            adapter.setIndexInDB();
            textView.setText("");
        });

    }


    private void saveDataPersonalGrowth(BigPlanAdapter bigPlanAdapter, TextView aim, int i) {

        String aimTextString = aim.getText().toString();
        int index = bigPlanAdapter.getItemCount();

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i, String.valueOf(index), aimTextString));

    }

    public void saveDataEvents(EventsListAdapter adapter, TextView plan, int i, String pickedDay, String newEvent) {

        String eventTextString;
        if (newEvent != null) {
            eventTextString = newEvent;
        } else {
            eventTextString = plan.getText().toString();
        }


        int index = adapter.getItemCount();

        EventsDao eventsDao = CalendarDatabase.getDatabase(getContext()).eventsDao();
        eventsDao.insert(new Event(String.valueOf(index), eventTextString, String.valueOf(index + 1), null, 0, pickedDay, 1));
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
        intent.setAction(ACTION_UPDATE_GRID_VIEW);
        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, CalendarWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}
