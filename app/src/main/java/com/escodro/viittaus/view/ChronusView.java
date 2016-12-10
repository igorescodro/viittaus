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
     * Radius of the clock.
     */
    private static final int CLOCK_RADIUS = 260;

    /**
     * Radius of the inner circle, that "holds" the pointers.
     */
    private static final int INNER_CIRCLE_RADIUS = 8;

    /**
     * The default pointer height, which is applied to minutes and seconds pointers.
     */
    private static final int DEFAULT_POINTER_HEIGHT = 230;

    /**
     * Pointer height to hours pointer. It must be lower than the other pointers.
     */
    private static final int HOURS_POINTER_HEIGHT = 153;

    /**
     * The negative value for seconds pointer. It is the value that will create the effect which the
     * seconds pointer is even bigger then others.
     */
    private static final int SECONDS_POINTER_TAIL = 35;

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
        init();
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

        final int secondAngle = getSecondsAngle(second);
        final int minuteAngle = getMinutesAngle(minute, second);
        final int hourAngle = getHoursAngle(hour, minute);

        canvas.drawCircle(halfWidth, halfHeight, CLOCK_RADIUS, mClockBackgroundPaint);

        drawHoursPointer(canvas, hourAngle, halfWidth, halfHeight);
        drawMinutesPointer(canvas, minuteAngle, halfWidth, halfHeight);
        drawSecondsPointer(canvas, secondAngle, halfWidth, halfHeight);

        canvas.drawCircle(halfWidth, halfHeight, INNER_CIRCLE_RADIUS, mDefaultPointerPaint);
    }

    /**
     * Calculate the second pointer angle in the clock. This angle is the position where the second
     * pointer will point in the clock.<br> The angle is calculated as follows:
     * <p/>
     * [Current second] * ([circumference] / [# of seconds in one hour])
     *
     * @param second current system seconds
     *
     * @return seconds angle
     */
    private int getSecondsAngle(int second) {
        return second * (360 / 60);
    }

    /**
     * Calculate the minute pointer angle in the clock. This angle is the position where the minute
     * pointer will point in the clock.<br> The angle is calculated as follows:
     * <p/>
     * <i>[current minute]</i> * (<i>[circumference]</i> / <i>[# of minutes in one hour]</i>) +
     * (<i>[current second]</i> / (<i>[# of minutes in one hour]</i> / (<i>[circumference]</i> /
     * <i>[# of seconds in one minute]</i>))
     * <p/>
     * The last sum in the equation is to keep the minutes pointer more fluid. It will move slowly
     * while the seconds pointer is moving, creating a better transition between minutes.
     *
     * @param minute current system minutes
     * @param second current system seconds
     *
     * @return minutes angle
     */
    private int getMinutesAngle(int minute, int second) {
        return minute * (360 / 60) + (second / (60 / (360 / 60)));
    }

    /**
     * Calculate the hour pointer angle in the clock. This angle is the position where the hour
     * pointer will point in the clock.<br> The angle is calculated as follows:
     * <p/>
     * <i>[current hour]</i> * (<i>[circumference]</i> / <i>[# of hours in one day (AM/PM)]</i>) +
     * (<i>[current minute]</i> / (<i>[# of hours in one day (AM/PM)]</i> / (<i>[circumference]</i>
     * / <i>[# of minutes in one hour]</i>))
     * <p/>
     * The last sum in the equation is to keep the hours pointer more fluid. It will move slowly
     * while the minutes pointer is moving, creating a better transition between hours.
     *
     * @param hour   current system hours
     * @param minute current system minutes
     *
     * @return hour angle
     */
    private int getHoursAngle(int hour, int minute) {
        return hour * (360 / 12) + (minute / (12 / (360 / 60)));
    }

    /**
     * Draw the hours pointer in the clock.
     *
     * @param canvas     canvas to be drawn
     * @param hourAngle  the angle of the hour pointer in the clock
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     */
    private void drawHoursPointer(Canvas canvas, float hourAngle, int halfWidth, int halfHeight) {
        final int pointerHeight = halfHeight - HOURS_POINTER_HEIGHT;
        canvas.save();
        canvas.rotate(hourAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, halfHeight, halfWidth, pointerHeight, mDefaultPointerPaint);
        canvas.restore();
    }

    /**
     * Draw the minutes pointer in the clock.
     *
     * @param canvas     canvas to be drawn
     * @param minAngle   the angle of the minute pointer in the clock
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     */
    private void drawMinutesPointer(Canvas canvas, float minAngle, int halfWidth, int halfHeight) {
        final int pointerHeight = halfHeight - DEFAULT_POINTER_HEIGHT;
        canvas.save();
        canvas.rotate(minAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, halfHeight, halfWidth, pointerHeight, mDefaultPointerPaint);
        canvas.restore();
    }

    /**
     * Draw the seconds pointer in the clock.
     *
     * @param canvas     canvas to be drawn
     * @param secAngle   the angle of the second pointer in the clock
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     */
    private void drawSecondsPointer(Canvas canvas, float secAngle, int halfWidth, int halfHeight) {
        final int pointerHeight = halfHeight - DEFAULT_POINTER_HEIGHT;
        final int pointerTail = halfHeight + SECONDS_POINTER_TAIL;
        canvas.save();
        canvas.rotate(secAngle, halfWidth, halfHeight);
        canvas.drawLine(halfWidth, pointerTail, halfWidth, pointerHeight, mSecondPointerPaint);
        canvas.restore();
    }
}
