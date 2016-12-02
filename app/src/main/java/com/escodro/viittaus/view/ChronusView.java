package com.escodro.viittaus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.escodro.viittaus.R;

import java.util.Calendar;

/**
 * Custom view to simulate a clock.
 * <p/>
 * Created by IgorEscodro on 27/11/16.
 */

public class ChronusView extends AnimatedSurfaceView {

    /**
     * Paint of the clock background.
     */
    private Paint mClockBackgroundPaint;

    /**
     * Paint of the seconds pointer.
     */
    private Paint mSecondPointerPaint;

    /**
     * Paint of the hours and minutes pointer.
     */
    private Paint mDefaultPointerPaint;

    /**
     * Create a new instance of {@link ChronusView}
     *
     * @param context application context
     */
    public ChronusView(Context context) {
        super(context);
    }

    /**
     * Create a new instance of {@link ChronusView}
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public ChronusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialize the view and its components.
     */
    private void init() {
        mClockBackgroundPaint = new Paint();
        mClockBackgroundPaint.setColor(getColor(R.color.player_gray_light));

        mSecondPointerPaint = new Paint();
        mSecondPointerPaint.setStyle(Paint.Style.STROKE);
        mSecondPointerPaint.setStrokeWidth(4);
        mSecondPointerPaint.setAntiAlias(true);
        mSecondPointerPaint.setColor(getColor(R.color.player_red));

        mDefaultPointerPaint = new Paint();
        mDefaultPointerPaint.setStrokeWidth(8);
        mDefaultPointerPaint.setAntiAlias(true);
        mDefaultPointerPaint.setColor(getColor(R.color.black));
    }

    @Override
    protected void onDrawOnCanvas(Canvas canvas) {
        final int halfWidth = canvas.getWidth() / 2;
        final int halfHeight = canvas.getHeight() / 2;
        final Calendar calendar = Calendar.getInstance();

        final int second = calendar.get(Calendar.SECOND);
        final int minute = calendar.get(Calendar.MINUTE);
        final int hour = calendar.get(Calendar.HOUR);

        final int secondAngle = second * (360 / 60);
        final int minuteAngle = minute * (360 / 60) + (second / 10);
        final int hourAngle = hour * (360 / 12) + (minute / 2);

        canvas.drawCircle(halfWidth, halfHeight, 260, mClockBackgroundPaint);

        canvas.save();
        canvas.rotate(hourAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, halfHeight, halfWidth, halfHeight - 153, mDefaultPointerPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(minuteAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, halfHeight, halfWidth, halfHeight - 230, mDefaultPointerPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate(secondAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, halfHeight + 35, halfWidth, halfHeight - 230,
                mSecondPointerPaint);
        canvas.restore();

        canvas.drawCircle(halfWidth, halfHeight, 8, mDefaultPointerPaint);
    }
}
