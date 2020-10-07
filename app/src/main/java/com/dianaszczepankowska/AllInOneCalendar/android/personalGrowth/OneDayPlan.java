package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.BigPlanData;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DateUtils.refactorStringIntoDate;

public class OneDayPlan extends Plan {

    private Context context;
    private LocalDate pickedDay;
    private LocalDate timeOutDate;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onPause() {
        super.onPause();
        CalendarUtils.saveEventsNumberToPickedDate(String.valueOf(pickedDay), context);
        //eventsListAdapter.setIndexInDB();
        addEffectivenesToDB(context, 4, firstItemDate, progress);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        question.setText(R.string.OneDayPlan);
        pickedDay = LocalDate.now();
        initData(this, adapter, planViewModel.getOneDayAimsList(), planViewModel.getOneDayAimsListIsChecked());
        setFabListener(adapter, 4, String.valueOf(pickedDay));
        return rootView;
    }


    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    @Override
    void initData(Fragment fragment, final PlansRecyclerViewAdapter adapter, LiveData<List<BigPlanData>> listLiveData, LiveData<List<BigPlanData>> listLiveDataIsChecked) {
        super.initData(fragment, adapter, listLiveData, listLiveDataIsChecked);
        listLiveData.observe(fragment, aims -> {
            adapter.setAimsList(aims);

            if (aims.size() != 0) {

                timeOutDate = refactorStringIntoDate(aims.get(0).getStartDate()).plusDays(1);
                long howManyHoursLeft = howMuchTimeLeft(timeOutDate).toHours();
                timeOut.setText(howManyHoursLeft + " " + getString(R.string.hoursLeft));

                if (timeOutDate.isBefore(LocalDate.now()) ||
                        timeOutDate.isEqual(LocalDate.now())) {
                    deleteIfTimeIsOut(4, context);

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

