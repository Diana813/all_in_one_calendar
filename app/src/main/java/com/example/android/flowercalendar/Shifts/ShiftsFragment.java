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

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.GestureInteractionsRecyclerView;
import com.example.android.flowercalendar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ShiftsFragment extends Fragment {

    private ShiftsAdapter adapter;
    private ShiftsViewModel shiftsViewModel;
    private Context context;
    private RelativeLayout empty_view;
    static int newId;

    public static ShiftsFragment newInstance() {
        return new ShiftsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new ShiftsAdapter(context);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.setIndexInDatabase();
        adapter.deleteFromDatabase();
    }

    public void onDetach() {
        super.onDetach();
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
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.Shifts));
        empty_view = view.findViewById(R.id.empty_view);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> showFragment(ShiftsEditor.newInstance(-1, null, null, null, null)));

        initData();
        return view;
    }

    private void showFragment(final Fragment fragment) {
        FragmentTransaction fragmentTransaction = (Objects.requireNonNull(getActivity())).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commitNow();
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
        builder.setPositiveButton(R.string.delete, (dialog, id) -> removeData());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void initData() {
        shiftsViewModel = new ViewModelProvider(this).get(ShiftsViewModel.class);
        shiftsViewModel.getShiftsList().observe(getViewLifecycleOwner(), shifts -> {
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
        });
    }

    private void removeData() {
        if (shiftsViewModel != null) {
            shiftsViewModel.deleteAll();
        }
    }

}
