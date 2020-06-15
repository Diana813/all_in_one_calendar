package com.example.android.flowercalendar.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.flowercalendar.utils.AppUtils;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanDao;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.StatisticsPersonalGrowth;
import com.example.android.flowercalendar.database.StatisticsPersonalGrowthDao;
import com.example.android.flowercalendar.gestures.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.statistics.StatisticsOfEffectiveness;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Plan extends Fragment {

    BigPlanAdapter adapter;
    private Context context;
    ProgressBar determinateBar;
    TextView effectiveness;
    TextView timeOut;
    LocalDate firstItemDate;
    int progress;
    int newId;
    RecyclerView recyclerView;
    EditText aimText;
    ImageButton confirm;
    TextView question;
    int layout;
    ViewGroup rootView;
    PlanViewModel planViewModel;


    public Plan() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new BigPlanAdapter(context);
    }


    @Override
    public void onPause() {
        super.onPause();
        adapter.deleteFromDatabase();
        adapter.setAimIndexInDB();
        AppUtils.hideKeyboard(getView(), context);
    }


    public void setContent(int layout) {
        this.layout = layout;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(layout, container, false);
        findViews(rootView);
        setHasOptionsMenu(true);
        setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            AppUtils.showDeleteConfirmationDialog(1);
            return true;

        } else if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(getActivity(), StatisticsOfEffectiveness.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    void initData(Fragment fragment, final BigPlanAdapter adapter, LiveData<List<BigPlanData>> listLiveData, androidx.lifecycle.LiveData<List<BigPlanData>> listLiveDataIsChecked) {
        listLiveData.observe(fragment, aims -> {
            adapter.setAimsList(aims);

            if (aims != null) {
                if (!aims.isEmpty()) {
                    String firstItemDateString = aims.get(0).getStartDate();
                    firstItemDate = AppUtils.refactorStringIntoDate(firstItemDateString);
                }

                newId = aims.size();
                listLiveDataIsChecked.observe(fragment, aimsList -> {
                    if (aimsList.size() != 0 && newId != 0) {
                        progress = aimsList.size() * 100 / newId;
                        determinateBar.setProgress(progress);
                        effectiveness.setVisibility(View.VISIBLE);
                        effectiveness.setText("Effectiveness: " + progress + "%");
                    } else if (aims.size() == 0) {
                        progress = -1;
                    } else {
                        progress = 0;
                        determinateBar.setProgress(0);
                        effectiveness.setVisibility(View.GONE);
                    }
                });
            }
        });
    }


    void deleteIfTimeIsOut(int i, Context context) {

        BigPlanDao bigPlanDao = CalendarDatabase.getDatabase(context).bigPlanDao();
        bigPlanDao.deleteAll(i);
    }


    int isTheTimeOut(List<BigPlanData> aims, int i) {

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
    Duration howMuchTimeLeft(LocalDate timeOutDate) {

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        ZonedDateTime timeout = ZonedDateTime.of(timeOutDate, LocalTime.of(0, 0, 0), zoneId);
        return Duration.between(now, timeout);
    }


    void addEffectivenesToDB(Context context, int i, LocalDate dateOfAddingFirstItem, int newEffectiveness) {

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


    void findViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.list);
        aimText = rootView.findViewById(R.id.editText);
        confirm = rootView.findViewById(R.id.confirm_button);
        question = rootView.findViewById(R.id.title);
        determinateBar = rootView.findViewById(R.id.determinateBar);
        effectiveness = rootView.findViewById(R.id.effectiveness);
        timeOut = rootView.findViewById(R.id.timeOut);
    }


    void setRecyclerViewPersonalGrowth(RecyclerView recyclerView, BigPlanAdapter adapter, Context context) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    void setItemTouchHelperPersonalGrowth(BigPlanAdapter adapter, RecyclerView recyclerView) {

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}


