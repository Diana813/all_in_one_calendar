package com.dianaszczepankowska.AllInOneCalendar.android.gestures;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.CyclicalEventsListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.EventsAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PlansRecyclerViewAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ShiftListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.WorkCalendarShiftListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.WorkListAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class GestureInteractionsRecyclerView extends ItemTouchHelper.SimpleCallback {
    private ShiftListAdapter shiftListAdapter;
    private CyclicalEventsListAdapter cyclicalEventsListAdapter;
    private PlansRecyclerViewAdapter plansRecyclerViewAdapter;
    private EventsAdapter eventsListAdapter;
    private WorkCalendarShiftListAdapter workCalendarShiftListAdapter;
    private WorkListAdapter workListAdapter;

    private Drawable icon;
    private final ColorDrawable background;

    public GestureInteractionsRecyclerView(ShiftListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        shiftListAdapter = adapter;
        icon = ContextCompat.getDrawable(ShiftListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(ShiftListAdapter.getContext(), R.color.lightestGreyZilla));
    }

    public GestureInteractionsRecyclerView(CyclicalEventsListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        cyclicalEventsListAdapter = adapter;
        icon = ContextCompat.getDrawable(CyclicalEventsListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(CyclicalEventsListAdapter.getContext(), R.color.lightestGreyZilla));
    }


    public GestureInteractionsRecyclerView(PlansRecyclerViewAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        plansRecyclerViewAdapter = adapter;
        icon = ContextCompat.getDrawable(PlansRecyclerViewAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(PlansRecyclerViewAdapter.getContext(), R.color.lightestGreyZilla));
    }

    public GestureInteractionsRecyclerView(EventsAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        eventsListAdapter = adapter;
        icon = ContextCompat.getDrawable(EventsAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(EventsAdapter.getContext(), R.color.lightestGreyZilla));
    }

    public GestureInteractionsRecyclerView(WorkCalendarShiftListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        workCalendarShiftListAdapter = adapter;
        icon = ContextCompat.getDrawable(WorkCalendarShiftListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(WorkCalendarShiftListAdapter.getContext(), R.color.lightestGreyZilla));
    }

    public GestureInteractionsRecyclerView(WorkListAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        workListAdapter = adapter;
        icon = ContextCompat.getDrawable(WorkListAdapter.getContext(),
                R.drawable.baseline_delete_black_36);
        background = new ColorDrawable(ContextCompat.getColor(WorkListAdapter.getContext(), R.color.lightestGreyZilla));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (workCalendarShiftListAdapter == null && cyclicalEventsListAdapter == null && plansRecyclerViewAdapter == null && eventsListAdapter == null && workListAdapter == null) {
            shiftListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else if (workCalendarShiftListAdapter == null && cyclicalEventsListAdapter == null && shiftListAdapter == null && eventsListAdapter == null && workListAdapter == null) {
            plansRecyclerViewAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else if (plansRecyclerViewAdapter == null && cyclicalEventsListAdapter == null && shiftListAdapter == null && eventsListAdapter == null && workListAdapter == null) {
            workCalendarShiftListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());

        } else if (plansRecyclerViewAdapter == null && cyclicalEventsListAdapter == null && shiftListAdapter == null && eventsListAdapter == null && workCalendarShiftListAdapter == null) {
            workListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        } else {
            assert eventsListAdapter != null;
            eventsListAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
        }

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (shiftListAdapter == null && plansRecyclerViewAdapter == null && eventsListAdapter == null
                && workListAdapter == null && workCalendarShiftListAdapter == null) {
            cyclicalEventsListAdapter.deleteItem(position);
        } else if (shiftListAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null && workListAdapter == null && workCalendarShiftListAdapter == null) {
            plansRecyclerViewAdapter.deleteItem(position);
        } else if (plansRecyclerViewAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null && workListAdapter == null && workCalendarShiftListAdapter == null) {
            shiftListAdapter.deleteItem(position);
        } else if (plansRecyclerViewAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null && shiftListAdapter == null && workCalendarShiftListAdapter == null) {
            workListAdapter.deleteItem(position);
        } else if (plansRecyclerViewAdapter == null && cyclicalEventsListAdapter == null && eventsListAdapter == null && workListAdapter == null && shiftListAdapter == null) {
            workCalendarShiftListAdapter.deleteItem(position);
        } else {
            assert eventsListAdapter != null;
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