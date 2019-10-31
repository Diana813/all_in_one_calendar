package com.example.android.flowercalendar.Shifts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Shift;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ShiftsFragment extends Fragment {

    private ShiftsAdapter adapter;
    private ShiftsViewModel shiftsViewModel;
    private Context context;
    private RelativeLayout empty_view;
    static int newId;
    private CoordinatorLayout coordinatorLayout;

    public static ShiftsFragment newInstance() {
        return new ShiftsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new ShiftsAdapter(context, context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.setIndexInDatabase();
        adapter.deleteFromDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shifts, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list_of_shifts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new GestureInteractionsRecyclerView(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setHasOptionsMenu(true);
        empty_view = (RelativeLayout) view.findViewById(R.id.empty_view);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFragment(ShiftsEditor.newInstance(-1, null, null, null, null));
            }
        });

        initData();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shifts_delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                removeData();
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


    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commitNow();
    }

    private void initData() {
        shiftsViewModel = ViewModelProviders.of(this).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(this, new Observer<List<Shift>>() {
            @Override
            public void onChanged(@Nullable List<Shift> shifts) {
                adapter.setShiftList(shifts);
                if (shifts == null) {
                    newId = 0;
                } else {
                    newId = shifts.size();
                }
                assert shifts != null;
                if (shifts.isEmpty()) {
                    empty_view.setVisibility(View.VISIBLE);
                } else {
                    empty_view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void removeData() {
        if (shiftsViewModel != null) {
            shiftsViewModel.deleteAll();
        }
    }


    private void reCreateDatabase() {
        CalendarDatabase.getDatabase(getContext()).clearDb();
    }

}