package com.example.android.flowercalendar.calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.flowercalendar.R;

import java.time.LocalDate;

import androidx.cardview.widget.CardView;

class CalendarUtils {

    private CalendarFragment calendarFragment;

   /* void setBackgroundDrawing(LocalDate date, LinearLayout layout) {

        Month currentMonth = date.getMonth();

        if (currentMonth.equals(Month.DECEMBER) || currentMonth.equals(Month.JANUARY) ||
                currentMonth.equals(Month.FEBRUARY)) {
            layout.setBackgroundResource(R.mipmap.klonzima);
        } else if (currentMonth.equals(Month.MARCH) || currentMonth.equals(Month.APRIL) ||
                currentMonth.equals(Month.MAY)) {
            layout.setBackgroundResource(R.mipmap.klonwiosna);
        } else if (currentMonth.equals(Month.JUNE) || currentMonth.equals(Month.JULY) ||
                currentMonth.equals(Month.AUGUST)) {
            layout.setBackgroundResource(R.mipmap.klonlato);
        } else if (currentMonth.equals(Month.SEPTEMBER) || currentMonth.equals(Month.OCTOBER) ||
                currentMonth.equals(Month.NOVEMBER)) {
            layout.setBackgroundResource(R.mipmap.klonjesien);
        }

    }*/

    //Animacja obrotu kalendarza
    void flipAnimation(CardView calendarCardView) {

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

    void showDeleteConfirmationDialog(final Context context, final LocalDate date, final TextView textView, final LinearLayout layout, final GridView gridView) {

        calendarFragment = new CalendarFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_all_dialog_message);
        builder.setPositiveButton(R.string.delete, (dialog, id) -> {
            calendarFragment.deleteAllShifts(date);
            calendarFragment.fillTheCalendar(context, textView, layout, gridView);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {

            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
