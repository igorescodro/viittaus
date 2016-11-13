package com.escodro.viittaus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.escodro.viittaus.R;

/**
 * Created by IgorEscodro on 13/11/16.
 */

public class RadarView extends SurfaceView implements Runnable {

    private static final int DELAY = 1000 / 30;

    private static final int PROGRESS = 5;

    private SurfaceHolder mHolder;

    private Thread mRenderThread;
    private boolean mRunning;
    private long mCurrentTime;
    private int mCurrentAngle = 60;

    private Paint mRadarPaint;
    private Paint mBorderPaint;
    private Paint mLinesPaint;

    private RectF mRadarRect;
    private RectF mBorderRect;

    private int[] mRadarColors;

    private int radarBorderWidth;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCurrentTime = System.currentTimeMillis();

        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setWillNotDraw(false);
        setZOrderOnTop(true);

        mRadarPaint = new Paint();

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(getColor(R.color.black));

        mLinesPaint = new Paint();
        mLinesPaint.setStyle(Paint.Style.STROKE);
        mLinesPaint.setAntiAlias(true);
        mLinesPaint.setColor(getColor(R.color.red_light));

        mRadarRect = new RectF();
        mBorderRect = new RectF();

        mRadarColors = new int[]{
                getColor(R.color.red_dark),
                getColor(R.color.red)};

    }

    protected void draw() {
        final Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        final int halfWidth = canvas.getWidth() / 2;
        final int halfHeight = canvas.getHeight() / 2;
        final int smallerDimension = getSmallerDimension(canvas);
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

        mHolder.unlockCanvasAndPost(canvas);
    }

    private void drawRadarBorder(Canvas canvas, int halfWidth, int halfHeight, int halfRadius) {
        mBorderRect.set(
                halfWidth - halfRadius - (radarBorderWidth / 2),
                halfHeight - halfRadius - (radarBorderWidth / 2),
                halfWidth + halfRadius + (radarBorderWidth / 2),
                halfHeight + halfRadius + (radarBorderWidth / 2));
        canvas.drawArc(mBorderRect, 0, 360, true, mBorderPaint);
    }

    private void drawRadar(Canvas canvas, int halfWidth, int halfHeight, int halfRadius) {
        mRadarPaint.setShader(new SweepGradient(halfWidth, halfHeight, mRadarColors, null));

        mRadarRect.set(
                halfWidth - halfRadius,
                halfHeight - halfRadius,
                halfWidth + halfRadius,
                halfHeight + halfRadius);
        canvas.drawArc(mRadarRect, 0, 360, true, mRadarPaint);
    }

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

    private int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    private int getSmallerDimension(Canvas canvas) {
        final int canvasHeight = canvas.getHeight();
        final int canvasWidth = canvas.getWidth();
        return canvasHeight < canvasWidth ? canvasHeight : canvasWidth;
    }

    @Override
    public void run() {
        while (mRunning) {
            if (!mHolder.getSurface().isValid()) {
                continue;
            }
            draw();
            long delay = (System.currentTimeMillis() - mCurrentTime);
            mCurrentTime = System.currentTimeMillis();

            try {
                if (delay < DELAY) {
                    Thread.sleep(DELAY - delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume() {
        mRunning = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    public void pause() {
        boolean retry = true;
        mRunning = false;
        while (retry) {
            try {
                mRenderThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
