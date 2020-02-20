package com.example.android.flowercalendar.PersonalGrowth;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.android.flowercalendar.GestureInteractionsRecyclerView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager.widget.ViewPager;

import static com.example.android.flowercalendar.PersonalGrowth.BigPlanAdapter.getContext;

class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        view.setTranslationX(-1 * view.getWidth() * position);

        if (position < -1) { // [-Infinity,-1)
            // This page is screen to the left.
            view.setAlpha(0f);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);

            //Hide keyboard
            final InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);



        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f);
        }
    }


}
