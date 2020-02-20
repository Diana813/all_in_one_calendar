package com.example.android.flowercalendar.PersonalGrowth;

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
import android.widget.TextView;

import com.example.android.flowercalendar.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ThisMonthPlan extends Fragment {

    private int layout;
    private BigPlanAdapter adapter;
    private Context context;
    private PersonalGrowthUtils personalGrowthUtils = new PersonalGrowthUtils();

    public ThisMonthPlan() {
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.setIndexInDatabase();
        adapter.deleteFromDatabase();
        personalGrowthUtils.hideKeyboard(getView());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

        RecyclerView recyclerView = rootView.findViewById(R.id.aimsList);
        EditText aimText = rootView.findViewById(R.id.aimText);
        ImageButton confirm = rootView.findViewById(R.id.confirm_button);
        TextView question = rootView.findViewById(R.id.title);
        question.setText(R.string.thisMonthPlan);
        ImageView imageView = rootView.findViewById(R.id.imageBackground);

        setHasOptionsMenu(true);
        personalGrowthUtils.displayImageFromDB(imageView);
        personalGrowthUtils.setRecyclerView(recyclerView,adapter,context);
        personalGrowthUtils.setItemTouchHelper(adapter, recyclerView);
        personalGrowthUtils.initData(this,adapter);
        personalGrowthUtils.setConfirmButton(confirm,adapter,aimText);
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
            personalGrowthUtils.showDeleteConfirmationDialog();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}

