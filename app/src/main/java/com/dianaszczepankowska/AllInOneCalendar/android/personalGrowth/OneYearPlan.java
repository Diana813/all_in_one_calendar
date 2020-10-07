package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.LanguageUtils.dayGramma;

public class OneYearPlan extends Plan {

    private Context context;
    private LocalDate timeOutDate;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        addEffectivenesToDB(context, 2, firstItemDate, progress);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        question.setText(R.string.thisYearsPlan);
        initData(this, adapter, planViewModel.getOneYearAimsList(), planViewModel.getOneYearAimsListIsChecked());
        setFabListener( adapter,2, LocalDate.now().toString());
        return rootView;
    }


    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    @Override
    void initData(Fragment fragment, final PlansRecyclerViewAdapter adapter, LiveData<List<BigPlanData>> listLiveData, LiveData<List<BigPlanData>> listLiveDataIsChecked) {
        super.initData(fragment, adapter, listLiveData, listLiveDataIsChecked);
        listLiveData.observe(fragment, aims -> {
            adapter.setAimsList(aims);
            if (aims.size() != 0) {

                String firstItemDateString = aims.get(0).getStartDate();
                firstItemDate = refactorStringIntoDate(firstItemDateString);

                int year = isTheTimeOut(aims, 2);

                timeOutDate = LocalDate.of(year, Month.JANUARY, 1).plusYears(1);

                long howManyDaysLeft = howMuchTimeLeft(timeOutDate).toDays();
                if (howManyDaysLeft == 1 || howManyDaysLeft < 1) {
                    howManyDaysLeft = howMuchTimeLeft(timeOutDate).toHours();
                    timeOut.setText((howManyDaysLeft + " " + getString(R.string.hoursLeft)));
                } else {
                    timeOut.setText(howManyDaysLeft + " " + dayGramma((int) howManyDaysLeft, context));
                }

                if (timeOutDate.isEqual(LocalDate.now()) ||
                        timeOutDate.isBefore(LocalDate.now())) {

                    deleteIfTimeIsOut(2, context);
                }
                if (newId == 0) {
                    timeOut.setVisibility(View.GONE);
                }

            } else {
                timeOut.setVisibility(View.GONE);
            }

        });

    }

}