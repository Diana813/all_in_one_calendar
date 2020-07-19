package com.dianaszczepankowska.AllInOneCalendar.android.gestures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.CyclicalEventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.ExpandableListHoursAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.EventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BigPlanAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class GestureInteractionsRecyclerView extends ItemTouchHelper.SimpleCallback {
    private ExpandableListHoursAdapter expandableListHoursAdapter;
    private ShiftsAdapter shiftsAdapter;
    private CyclicalEventsListAdapter cyclicalEventsListAdapter;
    private BigPlanAdapter bigPlanAdapter;
    private EventsListAdapter eventsListAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public GestureInteractionsRecyclerView(ShiftsAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        shiftsAdapter = adapter;
        icon = ContextCompat.getDrawable(ShiftsAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }

    public GestureInteractionsRecyclerView(CyclicalEventsListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        cyclicalEventsListAdapter = adapter;
        icon = ContextCompat.getDrawable(CyclicalEventsListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(Color.parseColor("#BDBDBD"));
    }


    public GestureInteractionsRecyclerView(ExpandableListHoursAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        expandableListHoursAdapter = adapter;
        background = new ColorDrawable(Color.parseColor("#ffffff"));
    }

    public GestureInteractionsRecyclerView(BigPlanAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        bigPlanAdapter = adapter;
        icon = ContextCompat.getDrawable(BigPlanAdapter.getContext(),
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

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (expandableListHoursAdapter == null && cyclicalEventsListAdapter == null && bigPlanAdapter == null && eventsListAdapter == null) {
            shiftsAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else if (expandableListHoursAdapter == null && cyclicalEventsListAdapter == null && shiftsAdapter == null && eventsListAdapter == null) {
            bigPlanAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } /*else if (bigPlanAdapter == null && cyclicalEventsListAdapter == null && shiftsAdapter == null && eventsListAdapter == null) {
            expandableListHoursAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } */ else {
            eventsListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }


        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (shiftsAdapter == null && bigPlanAdapter == null && eventsListAdapter == null
        ) {
            cyclicalEventsListAdapter.deleteItem(position);
        } else if (shiftsAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null) {
            bigPlanAdapter.deleteItem(position);
        } else if (bigPlanAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null) {
            shiftsAdapter.deleteItem(position);
        } else {
            eventsListAdapter.deleteItem(position);
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