package com.example.android.flowercalendar.PersonalGrowth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.MediaRouteButton;
import android.content.Context;
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
import com.example.android.flowercalendar.R;

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
    private EventsListAdapter eventsListAdapter;
    private ProgressBar determinateBar;
    private TextView effectiveness;


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
        eventsListAdapter = new EventsListAdapter(context);
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

        setHasOptionsMenu(true);
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this, adapter);
        appUtils.setConfirmButton(confirm, adapter, aimText, 2, null, eventsListAdapter, "0");
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
                        int progress = aimsList.size() * 100 / newId;
                        determinateBar.setProgress(progress);
                        effectiveness.setVisibility(View.VISIBLE);
                        effectiveness.setText("Effectiveness: " + progress + "%");
                    } else {
                        determinateBar.setProgress(0);
                        effectiveness.setVisibility(View.GONE);
                    }


                });
            }
        });

    }


}


