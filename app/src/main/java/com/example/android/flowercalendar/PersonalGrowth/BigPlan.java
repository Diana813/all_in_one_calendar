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

public class BigPlan extends Fragment {

    private int layout;
    private BigPlanAdapter adapter;
    private Context context;
    private AppUtils appUtils = new AppUtils();
    private EventsListAdapter eventsListAdapter;
    private ProgressBar determinateBar;
    private TextView effectiveness;


    public BigPlan() {
        // Required empty public constructor
    }

    public static BigPlan newInstance() {
        return new BigPlan();
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
        adapter.deleteFromDatabase();
        adapter.setAimIndexInDB();
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
        question.setText(R.string.fiveYearsPlan);
        ImageView imageView = rootView.findViewById(R.id.imageBackground);
        determinateBar = rootView.findViewById(R.id.determinateBar);
        effectiveness = rootView.findViewById(R.id.effectiveness);

        setHasOptionsMenu(true);
        appUtils.displayImageFromDB(imageView);
        appUtils.setRecyclerViewPersonalGrowth(recyclerView, adapter, context);
        appUtils.setItemTouchHelperPersonalGrowth(adapter, recyclerView);
        initData(this);
        appUtils.setConfirmButton(confirm, adapter, aimText, 1, null, eventsListAdapter, "0");
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
            appUtils.showDeleteConfirmationDialog(1);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint({"FragmentLiveDataObserve", "SetTextI18n"})
    private void initData(Fragment fragment) {
        BigPlanViewModel bigPlanViewModel = new ViewModelProvider(fragment).get(BigPlanViewModel.class);

        bigPlanViewModel.getAimsList().observe(fragment, aims -> {
            adapter.setAimsList(aims);
            int newId;
            if (aims != null) {
                newId = aims.size();
                bigPlanViewModel.getAimsListIsChecked().observe(fragment, aimsList -> {
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

