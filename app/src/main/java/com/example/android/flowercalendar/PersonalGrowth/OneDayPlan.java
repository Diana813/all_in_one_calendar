package com.example.android.flowercalendar.PersonalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.android.flowercalendar.Events.ExpandedDayView.ToDoList;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.StringsAims;
import com.example.android.flowercalendar.database.BigPlanData;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.lifecycle.ViewModelProviders.of;

public class OneDayPlan extends Fragment {

    private int layout;
    private BigPlanAdapter adapter;
    private Context context;
    private AppUtils appUtils = new AppUtils();
    private LocalDate pickedDay;
    private ToDoList toDoList;
    private RecyclerView recyclerView;
    private EditText aimText;
    private ImageButton confirm;
    private TextView question;
    private ImageView imageView;
    private CheckBox todayCheckbox;
    private CheckBox tomorrowCheckbox;


    public OneDayPlan() {
        // Required empty public constructor
    }

    public static OneDayPlan newInstance() {
        return new OneDayPlan();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new BigPlanAdapter(context, context);
        toDoList = new ToDoList();
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
        toDoList.setNumberOfEventsToPickedDate(String.valueOf(pickedDay));
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

        findViews(rootView);
        question.setText(R.string.OneDayPlan);
        todayCheckbox.setVisibility(View.VISIBLE);
        tomorrowCheckbox.setVisibility(View.VISIBLE);
        setHasOptionsMenu(true);
        pickedDay = LocalDate.now().plusDays(1);
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this, adapter);
        appUtils.setConfirmButton(confirm, adapter, aimText, 4, String.valueOf(pickedDay));
        return rootView;
    }

    private void findViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.list);
        aimText = rootView.findViewById(R.id.editText);
        confirm = rootView.findViewById(R.id.confirm_button);
        question = rootView.findViewById(R.id.title);
        imageView = rootView.findViewById(R.id.imageBackground);
        todayCheckbox = rootView.findViewById(R.id.checkboxToday);
        tomorrowCheckbox = rootView.findViewById(R.id.checkboxTomorrow);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            appUtils.showDeleteConfirmationDialog(4);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("FragmentLiveDataObserve")
    private void initData(Fragment fragment, final BigPlanAdapter adapter) {
        OneDayViewModel oneDayViewModel = of(fragment).get(OneDayViewModel.class);
        oneDayViewModel.getAimsList().observe(fragment, new Observer<List<BigPlanData>>() {
            @Override
            public void onChanged(@Nullable List<BigPlanData> aims) {
                adapter.setAimsList(aims);
            }
        });

    }


}

