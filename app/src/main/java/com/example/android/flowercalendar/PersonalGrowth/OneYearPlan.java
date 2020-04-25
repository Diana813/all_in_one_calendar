package com.example.android.flowercalendar.PersonalGrowth;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Statistics.StatisticsOfEffectiveness;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

public class OneYearPlan extends Fragment {

    private int layout;
    private BigPlanAdapter adapter;
    private Context context;
    private AppUtils appUtils = new AppUtils();
    private ProgressBar determinateBar;
    private TextView effectiveness;
    private LocalDate timeOutDate;
    private TextView timeOut;
    private int progress;
    private LocalDate firstItemDate;


    public OneYearPlan() {
        // Required empty public constructor
    }

    public static OneYearPlan newInstance() {
        return new OneYearPlan();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new BigPlanAdapter(context, context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.setAimIndexInDB();
        adapter.deleteFromDatabase();
        appUtils.hideKeyboard(getView(), context);
        appUtils.addEffectivenesToDB(context, 2, firstItemDate, progress);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setContent(int layout) {
        this.layout = layout;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(layout, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.LifeAims));


        RecyclerView recyclerView = rootView.findViewById(R.id.list);
        EditText aimText = rootView.findViewById(R.id.editText);
        ImageButton confirm = rootView.findViewById(R.id.confirm_button);
        TextView question = rootView.findViewById(R.id.title);
        question.setText(R.string.thisYearsPlan);
        ImageView imageView = rootView.findViewById(R.id.imageBackground);
        determinateBar = rootView.findViewById(R.id.determinateBar);
        effectiveness = rootView.findViewById(R.id.effectiveness);
        timeOut = rootView.findViewById(R.id.timeOut);

        setHasOptionsMenu(true);
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this, adapter);
        appUtils.setConfirmButton(confirm, adapter, aimText, 2, null, "0");
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
            appUtils.showDeleteConfirmationDialog(2);
            return true;

        } else if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(getActivity(), StatisticsOfEffectiveness.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    private void initData(Fragment fragment, final BigPlanAdapter adapter) {
        OneYearPlanViewModel oneYearPlanViewModel = new ViewModelProvider(fragment).get(OneYearPlanViewModel.class);
        oneYearPlanViewModel.getAimsList().observe(fragment, aims -> {
            adapter.setAimsList(aims);
            int newId;
            if (aims != null) {
                newId = aims.size();
                oneYearPlanViewModel.getAimsListIsChecked().observe(fragment, aimsList -> {
                    if (aimsList.size() != 0 && newId != 0) {
                        progress = aimsList.size() * 100 / newId;
                        determinateBar.setProgress(progress);
                        effectiveness.setVisibility(View.VISIBLE);
                        effectiveness.setText("Effectiveness: " + progress + "%");
                    } else {
                        progress = 0;
                        determinateBar.setProgress(0);
                        effectiveness.setVisibility(View.GONE);
                    }
                });

                if (aims.size() != 0) {

                    String firstItemDateString = aims.get(0).getStartDate();
                    firstItemDate = AppUtils.refactorStringIntoDate(firstItemDateString);

                    int year = appUtils.isTheTimeOut(aims, 2);

                    timeOutDate = LocalDate.of(year + 1, Month.JANUARY, 1);

                    long howManyDaysLeft = appUtils.howMuchTimeLeft(timeOutDate).toDays();
                    if (howManyDaysLeft == 1 || howManyDaysLeft < 1) {
                        howManyDaysLeft = appUtils.howMuchTimeLeft(timeOutDate).toHours();
                        timeOut.setText("Time left: " + howManyDaysLeft + " hours");
                    } else {
                        timeOut.setText("Time left: " + howManyDaysLeft + " days");
                    }

                    if (timeOutDate.isEqual(LocalDate.now()) ||
                            timeOutDate.isBefore(LocalDate.now())) {

                        appUtils.deleteIfTimeIsOut(2, context);
                    }
                    if (newId == 0) {
                        timeOut.setVisibility(View.GONE);
                    }

                } else {
                    timeOut.setVisibility(View.GONE);
                }
            }
        });

    }


}


