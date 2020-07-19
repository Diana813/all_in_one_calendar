package com.dianaszczepankowska.AllInOneCalendar.android.calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;

import java.time.LocalDate;
import java.util.List;

import androidx.cardview.widget.CardView;

import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;

public class CalendarUtils {

    //Animacja obrotu kalendarza
    static void flipAnimation(CardView calendarCardView) {

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(calendarCardView, "scaleX", 1f, 0f);
        final ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(calendarCardView, "scaleX", 0f, 1f);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                objectAnimator2.start();
            }
        });
        objectAnimator.start();
    }


    static void showDeleteConfirmationDialog(final Context context, final LocalDate date, final TextView textView, final GridView gridView) {

        CalendarFragment calendarFragment = new CalendarFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> {
            calendarFragment.deleteAllShifts(date);
            calendarFragment.fillTheCalendar(context, textView, gridView);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static void saveEventsNumberToPickedDate(String pickedDay, Context context) {

        EventsDao eventsDao = getDatabase(context).eventsDao();
        List<Event> listOfEvents = eventsDao.findByEventDate(pickedDay, 1, 3);
        int numberOfEvents = listOfEvents.size();

        CalendarEventsDao calendarEventsDao = getDatabase(context).calendarEventsDao();
        CalendarEvents eventToUpdate = calendarEventsDao.findBypickedDate(pickedDay);

        if (eventToUpdate != null) {

            if (!eventToUpdate.getEventsNumber().equals(String.valueOf(numberOfEvents))) {
                eventToUpdate.setEventsNumber(String.valueOf(numberOfEvents));
                calendarEventsDao.update(eventToUpdate);

            }
        } else {
            calendarEventsDao.insert(new CalendarEvents("", false, pickedDay, String.valueOf(numberOfEvents), null));
        }
    }
}
