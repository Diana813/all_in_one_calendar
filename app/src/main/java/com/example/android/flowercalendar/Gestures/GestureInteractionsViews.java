package com.example.android.flowercalendar.Gestures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

public class GestureInteractionsViews  implements View.OnTouchListener {

    private static final float MOVE_DENSITY = 50;
    private float startX;

    protected GestureInteractionsViews(Context ctx){
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                return false;
            case MotionEvent.ACTION_UP:
                float currentX = event.getX();
                if (this.startX-MOVE_DENSITY > currentX) {
                    onSwipeRight();
                    return true;
                }
                if (this.startX+MOVE_DENSITY < currentX) {
                    onSwipeLeft();
                    return true;
                }
            default:
                return false;
        }
    }


    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}
