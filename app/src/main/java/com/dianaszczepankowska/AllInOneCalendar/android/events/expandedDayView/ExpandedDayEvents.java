package com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.DeleteCyclicalEvent;
import com.dianaszczepankowska.AllInOneCalendar.android.events.eventsUtils.UtilsEvents;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class ExpandedDayEvents extends Fragment {

    public String pickedDay;
    public Context context;
    public DrawerLayout mDrawer;
    public NavigationView navigationView;
    public static int eventsListSize;

    public ExpandedDayEvents() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String findWhatDateItIs() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BackgroundActivityExpandedDayView) {
            pickedDay = ((BackgroundActivityExpandedDayView) parentFragment).getPickedDate();
        }
        return pickedDay;
    }

    public void showDeleteConfirmationDialog(int eventKind) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> removeData(eventKind));
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void removeData(int eventKind) {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        List<Event> eventsToDelete = eventsDao.sortByPickedDay(pickedDay, eventKind);

        DeleteCyclicalEvent deleteCyclicalEvent = new DeleteCyclicalEvent();
        List<Event> cyclicalEventsList = eventsDao.findByKind(0);

        for (Event eventToDelete : eventsToDelete) {
            if (cyclicalEventsList.isEmpty()) {
                return;
            }
            for (Event cyclicalEvent : cyclicalEventsList)
                if (cyclicalEvent.getEvent_name().equals(eventToDelete.getEvent_name())) {
                    deleteCyclicalEvent.deleteCyclicalEventFromPickedDay(cyclicalEvent.getEvent_name(), eventsDao);
                }
        }

        eventsDao.deleteByPickedDate(pickedDay, eventKind);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expanded_day_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void displayCyclicalEvents() {

        if (!CalendarFragment.listOfCyclicalEvents.isEmpty()) {

            for (String cyclicalEvent : CalendarFragment.listOfCyclicalEvents) {
                String[] parts = cyclicalEvent.split(";");

                if (parts[0].equals(pickedDay)) {
                    EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
                    Event event = eventsDao.findByEventKindAndName(parts[1], 0);
                    if (event != null && ((event.getSchedule() == null) || event.getSchedule().equals(""))) {
                        UtilsEvents.saveDataEvents(null, pickedDay, event.getEvent_name(), event.getFrequency(), event.getSchedule(), 1);
                    } else {
                        assert event != null;
                        UtilsEvents.saveDataEvents(null, pickedDay, event.getEvent_name(), event.getFrequency(), event.getSchedule(), 3);

                    }

                }
            }
        }
    }

    public static int positionOfTheNextEventOnTheList() {
        return eventsListSize + 1;
    }


}