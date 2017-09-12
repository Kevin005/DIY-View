package com.future.myapplication11.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Kevin on 2017/9/12.
 */

public class MarqueeTextC extends TextView implements Runnable {
    private boolean isStop = false;
    private int currentLoc = 0;
    private boolean measureText = true;
    private int textWidth;

    public MarqueeTextC(Context context) {
        super(context);
    }

    public MarqueeTextC(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarqueeTextC(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (measureText) {
            Paint paint = this.getPaint();
            textWidth = (int) paint.measureText(getText().toString());
            measureText = false;
        }
    }

    @Override
    public void run() {
        if (isStop) return;
        currentLoc += 2;
        scrollTo(currentLoc, 0);
        if (currentLoc > getWidth()) {
            currentLoc = -getWidth() + (getWidth() - textWidth) / 2;
            scrollTo(currentLoc, 0);
        }
        postDelayed(this, 20);
    }

    public void startScroll() {
        isStop = false;
        post(this);
    }


    public void stopScroll() {
        isStop = true;
        removeCallbacks(this);
    }
}
