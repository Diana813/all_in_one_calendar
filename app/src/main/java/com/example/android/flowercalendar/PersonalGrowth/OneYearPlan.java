package com.example.android.flowercalendar.PersonalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.BigPlanData;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.lifecycle.ViewModelProviders.of;

public class OneYearPlan extends Fragment {

    private int layout;
    private BigPlanAdapter adapter;
    private Context context;
    private AppUtils appUtils = new AppUtils();
    private int newId;
    private LocalDate today;
    private LocalDate tomorrow;
    private EventsListAdapter eventsListAdapter;
    private CheckBox todayCheckbox;
    private CheckBox tomorrowCheckbox;
    private LocalDate pickedDay;

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
        eventsListAdapter = new EventsListAdapter(context, context);
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
        appUtils.hideKeyboard(getView());
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
        todayCheckbox = rootView.findViewById(R.id.checkboxToday);
        tomorrowCheckbox = rootView.findViewById(R.id.checkboxTomorrow);

        todayCheckbox.setOnCheckedChangeListener(myCheckboxListener);
        tomorrowCheckbox.setOnCheckedChangeListener(myCheckboxListener);
        setHasOptionsMenu(true);
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this, adapter);
        appUtils.setConfirmButton(confirm, adapter, aimText, 2);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            appUtils.showDeleteConfirmationDialog(2);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private LocalDate findTodaysDate() {
        return LocalDate.now();
    }

    private LocalDate findTomorrowsDate() {
        tomorrow = findTodaysDate().plusDays(1);
        return tomorrow;
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void initData(Fragment fragment, final BigPlanAdapter adapter) {
        OneYearPlanViewModel oneYearPlanViewModel = of(fragment).get(OneYearPlanViewModel.class);
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


   /* private void setTodayCheckbox() {

        todayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    pickedDay = findTodaysDate();

                }
            }
        });
    }


    private void setTomorrowCheckbox() {

        tomorrowCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    pickedDay = findTodaysDate();

                }
            }
        });
    }
*/
    private CompoundButton.OnCheckedChangeListener myCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.checkboxToday:
                    pickedDay = findTodaysDate();
                    break;
                case R.id.checkboxTomorrow:
                    pickedDay = findTomorrowsDate();
                    break;
                default:
                    break;
            }
        }
    };

}


