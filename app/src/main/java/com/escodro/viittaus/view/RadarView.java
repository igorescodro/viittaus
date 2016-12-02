package com.escodro.viittaus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;

import com.escodro.viittaus.R;

/**
 * Custom view to simulate a radar.
 * <p/>
 * Created by IgorEscodro on 13/11/16.
 */

public class RadarView extends AnimatedSurfaceView {

    /**
     * Progress of the radar in degrees for each frame.
     */
    private static final int PROGRESS = 5;

    /**
     * Integer to control de rotation of the radar.
     */
    private int mCurrentAngle = 60;

    /**
     * Paint of the radar.
     */
    private Paint mRadarPaint;

    /**
     * Paint of the border of the radar.
     */
    private Paint mBorderPaint;

    /**
     * Paint of the lines of the radar.
     */
    private Paint mLinesPaint;

    /**
     * {@link RectF} with the radar dimensions.
     */
    private RectF mRadarRect;

    /**
     * {@link RectF} with the border dimension of the radar.
     */
    private RectF mBorderRect;

    /**
     * Array with the radar colors to be used in the {@link SweepGradient}.
     */
    private int[] mRadarColors;

    /**
     * Width of the radar border.
     */
    private int radarBorderWidth;

    /**
     * Create a new instance of {@link RadarView}
     *
     * @param context application context
     */
    public RadarView(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance of {@link RadarView}
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialize the view and its components.
     */
    private void init() {
        mRadarPaint = new Paint();

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(getColor(R.color.black));

        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setColor(getColor(R.color.radar_red_light));

        mRadarRect = new RectF();
        mBorderRect = new RectF();

        mRadarColors = new int[]{
                getColor(R.color.radar_red_dark),
                getColor(R.color.radar_red)};

    }

    @Override
    protected void onDrawOnCanvas(Canvas canvas) {
        final int halfWidth = canvas.getWidth() / 2;
        final int halfHeight = canvas.getHeight() / 2;
        final int smallerDimension = getSmallestDimension(canvas);
        final int halfRadius = smallerDimension / 3;
        final int mLinesRadarWidth = smallerDimension / 128;
        radarBorderWidth = smallerDimension / 32;

        mBorderPaint.setStrokeWidth(radarBorderWidth);
        mLinesPaint.setStrokeWidth(mLinesRadarWidth);

        mCurrentAngle += PROGRESS;

        canvas.save();
        canvas.rotate(mCurrentAngle, halfWidth, halfHeight);
        drawRadar(canvas, halfWidth, halfHeight, halfRadius);
        canvas.restore();

        drawRadarBorder(canvas, halfWidth, halfHeight, halfRadius);
        drawRadarLines(canvas, halfWidth, halfHeight, halfRadius);
    }

    /**
     * Draw the radar border.
     *
     * @param canvas     canvas to be drawn
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     * @param halfRadius half radius of the radar
     */
    private void drawRadarBorder(Canvas canvas, int halfWidth, int halfHeight, int halfRadius) {
        mBorderRect.set(
                halfWidth - halfRadius - (radarBorderWidth / 2),
                halfHeight - halfRadius - (radarBorderWidth / 2),
                halfWidth + halfRadius + (radarBorderWidth / 2),
                halfHeight + halfRadius + (radarBorderWidth / 2));
        canvas.drawArc(mBorderRect, 0, 360, true, mBorderPaint);
    }

    /**
     * Draw the radar.
     *
     * @param canvas     canvas to be drawn
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     * @param halfRadius half radius of the radar
     */
    private void drawRadar(Canvas canvas, int halfWidth, int halfHeight, int halfRadius) {
        mRadarPaint.setShader(new SweepGradient(halfWidth, halfHeight, mRadarColors, null));

        mRadarRect.set(
                halfWidth - halfRadius,
                halfHeight - halfRadius,
                halfWidth + halfRadius,
                halfHeight + halfRadius);
        canvas.drawArc(mRadarRect, 0, 360, true, mRadarPaint);
    }

    /**
     * Draw the radar lines.
     *
     * @param canvas     canvas to be drawn
     * @param halfWidth  half width of the canvas
     * @param halfHeight half height of the canvas
     * @param halfRadius half radius of the radar
     */
    private void drawRadarLines(Canvas canvas, int halfWidth, int halfHeight, int halfRadius) {
        canvas.drawCircle(halfWidth, halfHeight, halfRadius / 3, mLinesPaint);
        canvas.drawCircle(halfWidth, halfHeight, halfRadius / 1.5F, mLinesPaint);
        canvas.drawCircle(halfWidth, halfHeight, halfRadius, mLinesPaint);

        canvas.drawLine(
                halfWidth,
                halfHeight - halfRadius,
                halfWidth,
                halfHeight + halfRadius,
                mLinesPaint);

        canvas.drawLine(
                halfWidth - halfRadius,
                halfHeight,
                halfWidth + halfRadius,
                halfHeight,
                mLinesPaint);

        canvas.drawLine(
                halfWidth - halfRadius * 0.7F,
                halfHeight - halfRadius * 0.7F,
                halfWidth + halfRadius * 0.7F,
                halfHeight + halfRadius * 0.7F,
                mLinesPaint);

        canvas.drawLine(
                halfWidth + halfRadius * 0.7F,
                halfHeight - halfRadius * 0.7F,
                halfWidth - halfRadius * 0.7F,
                halfHeight + halfRadius * 0.7F,
                mLinesPaint
        );
    }

    /**
     * Get the smallest dimension (width or height) of the canvas. This method is needed to
     * guarantee that the view will always be fully shown, even if the dimensions are not square.
     *
     * @param canvas radar canvas
     *
     * @return the smallest view
     */
    private int getSmallestDimension(Canvas canvas) {
        final int canvasHeight = canvas.getHeight();
        final int canvasWidth = canvas.getWidth();
        return canvasHeight < canvasWidth ? canvasHeight : canvasWidth;
    }
}
