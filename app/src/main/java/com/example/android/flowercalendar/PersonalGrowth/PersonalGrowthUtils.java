package com.example.android.flowercalendar.PersonalGrowth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.ImagePath;
import com.example.android.flowercalendar.database.ImagePathDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter.getContext;
import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

class PersonalGrowthUtils {

    private int newId;

    void setRecyclerView(RecyclerView recyclerView, BigPlanAdapter adapter, Context context){

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    void setItemTouchHelper(BigPlanAdapter adapter, RecyclerView recyclerView){

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BigPlanDao bigPlanDao = (BigPlanDao) CalendarDatabase.getDatabase(getContext()).bigPlanDao();
                bigPlanDao.deleteAll();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void hideKeyboard(View view) {

        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(view).getWindowToken(), 0);
        }
    }

    void setConfirmButton(ImageButton confirm, final BigPlanAdapter adapter, final TextView textView, final int i) {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(textView, i);
                adapter.setIndexInDatabase();
                textView.setText("");
            }
        });

    }

    private void saveData(TextView textView, int i) {

        String aimTextString = textView.getText().toString();

        BigPlanDao bigPlanDao = (BigPlanDao) CalendarDatabase.getDatabase(getContext()).bigPlanDao();
        bigPlanDao.insert(new BigPlanData(i,String.valueOf((newId + 1) + "."), aimTextString));

    }

    void initDataBigPlan(Fragment fragment, final BigPlanAdapter adapter) {
        BigPlanViewModel bigPlanViewModel = ViewModelProviders.of(fragment).get(BigPlanViewModel.class);
        bigPlanViewModel.getAimsList().observe(fragment, new Observer<List<BigPlanData>>() {
            @Override
            public void onChanged(@Nullable List<BigPlanData> aims) {
                adapter.setAimsList(aims);
                if (aims == null) {
                    newId = 0;
                } else {
                    newId = aims.size();
                }
            }
        });

    }

    void initDataOneYear(Fragment fragment, final BigPlanAdapter adapter) {
        OneYearPlanViewModel oneYearPlanViewModel = ViewModelProviders.of(fragment).get(OneYearPlanViewModel.class);
        oneYearPlanViewModel.getAimsList().observe(fragment, new Observer<List<BigPlanData>>() {
            @Override
            public void onChanged(@Nullable List<BigPlanData> aims) {
                adapter.setAimsList(aims);
                if (aims == null) {
                    newId = 0;
                } else {
                    newId = aims.size();
                }
            }
        });

    }

    void initDataThisMonth(Fragment fragment, final BigPlanAdapter adapter) {
        ThisMonthViewModel thisMonthViewModel = ViewModelProviders.of(fragment).get(ThisMonthViewModel.class);
        thisMonthViewModel.getAimsList().observe(fragment, new Observer<List<BigPlanData>>() {
            @Override
            public void onChanged(@Nullable List<BigPlanData> aims) {
                adapter.setAimsList(aims);
                if (aims == null) {
                    newId = 0;
                } else {
                    newId = aims.size();
                }
            }
        });

    }

    void initDataOneDay(Fragment fragment, final BigPlanAdapter adapter) {
        OneDayViewModel oneDayViewModel = ViewModelProviders.of(fragment).get(OneDayViewModel.class);
        oneDayViewModel.getAimsList().observe(fragment, new Observer<List<BigPlanData>>() {
            @Override
            public void onChanged(@Nullable List<BigPlanData> aims) {
                adapter.setAimsList(aims);
                if (aims == null) {
                    newId = 0;
                } else {
                    newId = aims.size();
                }
            }
        });

    }


    void displayImageFromDB(ImageView view) {

        ImagePathDao imagePathDao = getDatabase(getContext()).imagePathDao();
        ImagePath imagePath = imagePathDao.findLastImage();
        if (imagePath != null) {
            String aimImagePath = imagePath.getImagePath();
            if (aimImagePath.contains("imageDir")) {
                loadImageFromStorage(aimImagePath,view);
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
}
