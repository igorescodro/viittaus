package com.escodro.viittaus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Abstract {@link SurfaceView} to draw animated views.
 * <p/>
 * Created by IgorEscodro on 26/11/16.
 */

public abstract class AnimatedSurfaceView extends SurfaceView implements Runnable {

    /**
     * Refresh rate (frames per second).
     */
    private static int mDelay;

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
     * Create a new instance of {@link AnimatedSurfaceView}.
     *
     * @param context application context
     */
    public AnimatedSurfaceView(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance of {@link AnimatedSurfaceView}.
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public AnimatedSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    /**
     * Initialize the {@link SurfaceView} with default configuration.
     */
    private void init() {
        mCurrentTime = System.currentTimeMillis();
        mHolder = getHolder();

        setSurfaceHolderFormat(PixelFormat.TRANSPARENT);
        setFrameRate(30);
        setWillNotDraw(false);
        setZOrderOnTop(true);
    }

    /**
     * Set the frame rate in frames per second
     *
     * @param framePerSecond frames per second
     */
    protected void setFrameRate(int framePerSecond) {
        if (framePerSecond > 0) {
            mDelay = 1000 / framePerSecond;
        }
    }

    /**
     * Set the desired PixelFormat of the surface. The default is OPAQUE. When working with a {@link
     * SurfaceView}, this must be called from the same thread running the SurfaceView's window.
     *
     * @param format a constant from PixelFormat
     */
    protected void setSurfaceHolderFormat(int format) {
        mHolder.setFormat(format);
    }

    /**
     * Method to draw on the canvas. There is no need to lock or unlock and post the canvas.
     *
     * @param canvas canvas to be drawn
     */
    protected abstract void onDrawOnCanvas(Canvas canvas);

    /**
     * Get the color from {@link ColorRes} id.
     *
     * @param colorId color id
     *
     * @return color
     */
    protected int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    /**
     * Lock the canvas and draw a transparent background. After the draw done by children in {@link
     * AnimatedSurfaceView#onDrawOnCanvas(Canvas)} unlock and post in the screen.
     */
    private void drawOnCanvas() {
        final Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        onDrawOnCanvas(canvas);

        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        while (mRunning) {
            if (!mHolder.getSurface().isValid()) {
                continue;
            }
            drawOnCanvas();
            long delay = (System.currentTimeMillis() - mCurrentTime);
            mCurrentTime = System.currentTimeMillis();

            try {
                if (delay < mDelay) {
                    Thread.sleep(mDelay - delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start the thread on resume the life-cycle.
     */
    public void resume() {
        mRunning = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    /**
     * Pause the thread on pause the life-cycle.
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
