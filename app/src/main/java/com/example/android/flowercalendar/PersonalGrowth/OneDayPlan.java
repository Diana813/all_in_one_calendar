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
import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.Events.EventsViewModel;
import com.example.android.flowercalendar.Events.ExpandedDayView.ToDoList;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.Statistics.StatisticsOfEffectiveness;

import java.time.LocalDate;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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
    private ProgressBar determinateBar;
    private TextView effectiveness;
    private EventsListAdapter eventsListAdapter;
    private LocalDate timeOutDate;
    private TextView timeOut;
    private int progress;
    private LocalDate firstItemDate;


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
        eventsListAdapter = new EventsListAdapter(EventsListAdapter.getContext());

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
        toDoList.setNumberOfEventsToPickedDate(String.valueOf(pickedDay));
        eventsListAdapter.setIndexInDB();
        appUtils.addEffectivenesToDB(context, 4, firstItemDate, progress);
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
        setHasOptionsMenu(true);
        pickedDay = LocalDate.now();
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this, adapter);
        initDataEvents();
        appUtils.setConfirmButton(confirm, adapter, aimText, 4, String.valueOf(pickedDay), "-1", null, 1);
        return rootView;
    }

    private void findViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.list);
        aimText = rootView.findViewById(R.id.editText);
        confirm = rootView.findViewById(R.id.confirm_button);
        question = rootView.findViewById(R.id.title);
        imageView = rootView.findViewById(R.id.imageBackground);
        determinateBar = rootView.findViewById(R.id.determinateBar);
        effectiveness = rootView.findViewById(R.id.effectiveness);
        timeOut = rootView.findViewById(R.id.timeOut);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            appUtils.showDeleteConfirmationDialog(4);
            return true;

        } else if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(getActivity(), StatisticsOfEffectiveness.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    private void initData(Fragment fragment, final BigPlanAdapter adapter) {
        OneDayViewModel oneDayViewModel = new ViewModelProvider(fragment).get(OneDayViewModel.class);
        oneDayViewModel.getAimsList().observe(fragment, aims -> {
            adapter.setAimsList(aims);
            int newId;

            if (aims != null) {
                if (!aims.isEmpty()) {
                    String firstItemDateString = aims.get(0).getStartDate();
                    firstItemDate = AppUtils.refactorStringIntoDate(firstItemDateString);
                }


                newId = aims.size();
                oneDayViewModel.getAimsListIsChecked().observe(fragment, aimsList -> {
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

                if (aims.size() != 0) {

                    int month = LocalDate.now().getMonthValue();
                    int year = LocalDate.now().getYear();
                    int dayOfMonth = appUtils.isTheTimeOut(aims, 4);

                    timeOutDate = LocalDate.of(year, month, dayOfMonth + 1);
                    long howManyHoursLeft = appUtils.howMuchTimeLeft(timeOutDate).toHours();
                    timeOut.setText("Time left: " + howManyHoursLeft + " hours");

                    if (timeOutDate.isBefore(LocalDate.now()) ||
                            timeOutDate.isEqual(LocalDate.now())) {
                        appUtils.deleteIfTimeIsOut(4, context);

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

    private void initDataEvents() {

        EventsViewModel eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        eventsViewModel.getEventsListForAims().observe(getViewLifecycleOwner(), events -> {
            assert events != null;
            eventsListAdapter.setEventsList(events);
        });

    }


}

