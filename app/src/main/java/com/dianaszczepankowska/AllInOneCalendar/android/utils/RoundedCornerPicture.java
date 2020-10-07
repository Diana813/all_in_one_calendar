package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class RoundedCornerPicture extends ImageView {

    private Path path;

    public RoundedCornerPicture(Context context) {
        super(context);
        init();
    }

    public RoundedCornerPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        float radius = 25.0f;
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}

