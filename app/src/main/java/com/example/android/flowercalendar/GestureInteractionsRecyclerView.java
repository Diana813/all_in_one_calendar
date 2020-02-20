package com.example.android.flowercalendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.android.flowercalendar.Events.EventsListAdapter;
import com.example.android.flowercalendar.Events.EventsListHoursAdapter;
import com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter;
import com.example.android.flowercalendar.Shifts.ShiftsAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class GestureInteractionsRecyclerView extends ItemTouchHelper.SimpleCallback {
    private EventsListHoursAdapter testingListAdapter;
    private ShiftsAdapter shiftsAdapter;
    private EventsListAdapter eventsListAdapter;
    private BigPlanAdapter bigPlanAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public GestureInteractionsRecyclerView(ShiftsAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        shiftsAdapter = adapter;
        icon = ContextCompat.getDrawable(ShiftsAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }

    public GestureInteractionsRecyclerView(EventsListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        eventsListAdapter = adapter;
        icon = ContextCompat.getDrawable(EventsListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }


    public GestureInteractionsRecyclerView(EventsListHoursAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        testingListAdapter = adapter;
        icon = ContextCompat.getDrawable(EventsListHoursAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }

    public GestureInteractionsRecyclerView(BigPlanAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        bigPlanAdapter = adapter;
        icon = ContextCompat.getDrawable(BigPlanAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (shiftsAdapter == null && testingListAdapter == null && bigPlanAdapter == null) {
            eventsListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else if (testingListAdapter == null && eventsListAdapter == null && bigPlanAdapter == null) {
            shiftsAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }else if (testingListAdapter == null && eventsListAdapter == null && shiftsAdapter == null){
            bigPlanAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else {
            assert testingListAdapter != null;
            testingListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }


        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (shiftsAdapter == null && bigPlanAdapter == null) {
            eventsListAdapter.deleteItem(position);
        }else if(shiftsAdapter == null && eventsListAdapter == null){
            bigPlanAdapter.deleteItem(position);
        } else {
            assert shiftsAdapter != null;
            shiftsAdapter.deleteItem(position);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() + icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}