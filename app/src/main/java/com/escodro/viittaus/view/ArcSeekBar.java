package com.escodro.viittaus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.escodro.viittaus.R;

/**
 * Custom Seek Bar in arc format.
 * <p/>
 * Created by Igor Escodro on 17/5/2016.
 */
public class ArcSeekBar extends SurfaceView implements Runnable {

    /**
     * Refresh rate of 30 frames per second.
     */
    private static final int DELAY = 1000 / 30;

    /**
     * {@link SurfaceHolder} reference.
     */
    private SurfaceHolder mHolder;

    /**
     * Thread to handle the view animation.
     */
    private Thread mRenderThread;

    /**
     * Boolean to control the view animation.
     */
    private boolean mRunning;

    /**
     * Long to control the frame rate.
     */
    private long mCurrentTime;

    /**
     * The primary color of the seek bar.
     */
    private Paint mPaintPrimary = null;

    /**
     * The secondary color of the seek bar.
     */
    private Paint mPaintSecondary = null;

    /**
     * {@link RectF} with the seek bar measures.
     */
    private RectF mSeekbarArea = null;

    /**
     * The left position of the {@link ArcSeekBar} in the {@link Canvas}
     */
    private int mLeftPos;

    /**
     * The top position of the {@link ArcSeekBar} in the {@link Canvas}
     */
    private int mTopPos;

    /**
     * The right position of the {@link ArcSeekBar} in the {@link Canvas}
     */
    private int mRightPos;

    /**
     * The bottom position of the {@link ArcSeekBar} in the {@link Canvas}
     */
    private int mBottomPos;

    /**
     * Field to store the maximum time of the {@link ArcSeekBar}.
     */
    private int mMaxProgress;

    /**
     * Field to store the current progress of the {@link ArcSeekBar}.
     */
    private int mCurrentProgress;

    /**
     * Field to store the customizable inner margin of the {@link ArcSeekBar}.
     */
    private int mSeekBarMargin;

    /**
     * Create a new instance of {@link ArcSeekBar}.
     *
     * @param context application context
     */
    public ArcSeekBar(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance of {@link ArcSeekBar}.
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public ArcSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            setZOrderOnTop(true);
            mHolder = getHolder();
            mHolder.setFormat(PixelFormat.TRANSPARENT);
            mCurrentTime = System.currentTimeMillis();
            mMaxProgress = 100;
            mSeekBarMargin = 25;
            init();
        }
    }

    /**
     * Initialize the basic components.
     */
    private void init() {
        mPaintPrimary = new Paint();
        mPaintPrimary.setColor(getColor(R.color.green_light));
        mPaintPrimary.setStyle(Paint.Style.STROKE);
        mPaintPrimary.setStrokeWidth(5);
        mPaintPrimary.setAntiAlias(true);

        mPaintSecondary = new Paint();
        mPaintSecondary.setColor(getColor(R.color.gray));
        mPaintSecondary.setStyle(Paint.Style.STROKE);
        mPaintSecondary.setStrokeWidth(5);
        mPaintSecondary.setAntiAlias(true);

        mSeekbarArea = new RectF();
    }

    /**
     * Sets the inner margin between the center and the {@link ArcSeekBar}.
     *
     * @param margin inner margin
     */
    public void setSeekBarMargin(int margin) {
        mSeekBarMargin = margin;
    }

    /**
     * Set the album cover width and height.
     *
     * @param albumWidth  album cover width
     * @param albumHeight album cover height
     */
    public void setAlbumSize(int albumWidth, int albumHeight) {
        final int halfAlbumWidth = albumWidth / 2;
        final int halfAlbumHeight = albumHeight / 2;
        mLeftPos = halfAlbumWidth + mSeekBarMargin;
        mTopPos = halfAlbumHeight + mSeekBarMargin;
        mRightPos = halfAlbumWidth + mSeekBarMargin;
        mBottomPos = halfAlbumHeight + mSeekBarMargin;
    }

    /**
     * Sets the range of the {@link ArcSeekBar}.
     *
     * @param maxProgress the upper range
     */
    public void setMax(int maxProgress) {
        if (maxProgress < 1) {
            mMaxProgress = 1;
        } else {
            mMaxProgress = maxProgress;
        }
    }

    /**
     * Sets the current progress of the {@link ArcSeekBar}.
     *
     * @param progress the current progress
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
    }

    /**
     * Gets the current progress of the {@link ArcSeekBar}.
     *
     * @return the current progress
     */
    private float getProgress() {
        final float progress;
        if (mCurrentProgress == mMaxProgress) {
            progress = 225;
        } else {
            progress = ((float) 225 / mMaxProgress) * mCurrentProgress;
        }
        return progress;
    }

    /**
     * Draws the {@link ArcSeekBar}.
     */
    private void draw() {
        final Canvas canvas = mHolder.lockCanvas();
        final int halfWidth = canvas.getWidth() / 2;
        final int halfHeight = canvas.getHeight() / 2;
        mSeekbarArea.set(
                halfWidth - mLeftPos,
                halfHeight - mTopPos,
                halfWidth + mRightPos,
                halfHeight + mBottomPos);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawArc(mSeekbarArea, 157.5F, 225, false, mPaintSecondary);
        canvas.drawArc(mSeekbarArea, 157.5F, getProgress(), false, mPaintPrimary);
        mHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * Get the color from {@link ColorRes} id.
     *
     * @param colorId color id
     *
     * @return color
     */
    private int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
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

    /**
     * Starts the thread on resume the life-cycle.
     */
    public void resume() {
        mRunning = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    /**
     * Pauses the thread on pause the life-cycle.
     */
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
