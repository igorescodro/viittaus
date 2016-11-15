package com.escodro.viittaus.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.escodro.viittaus.R;

import java.util.LinkedList;

/**
 * Custom view to simulate an audio spectrum.
 */
public class SpectreView extends SurfaceView implements Runnable {

    /**
     * Half height of center separator.
     */
    private static final int SEPARATOR_HALF_HEIGHT = 2;

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
     * List with all the volume values.
     */
    private final LinkedList<Integer> mVolumeList;

    /**
     * Boolean to represent if the center separator should be shown.
     */
    private boolean mAttrShowSeparator;

    /**
     * Integer to represent the custom bar color of the spectrum.
     */
    private int mAttrBarColor;

    /**
     * Integer to represent the custom background color of the spectrum.
     */
    private int mAttrBackgroundColor;

    /**
     * Integer to represent the custom quantity of bars in the spectrum.
     */
    private int mAttrBarCount;

    /**
     * Integer to represent the bar width.
     */
    private int mBarWidth;

    /**
     * Integer to represent the margin of each bar in the spectrum.
     */
    private int mMargin;

    /**
     * Create a new instance of {@link SpectreView}.
     *
     * @param context application context
     */
    public SpectreView(Context context) {
        this(context, null);
    }

    /**
     * Create a new instance of {@link SpectreView}.
     *
     * @param context application context
     * @param attrs   attribute set
     */
    public SpectreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateAttributeSetValues(attrs, context);
        mHolder = getHolder();
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mCurrentTime = System.currentTimeMillis();
        mVolumeList = new LinkedList<>();
    }

    /**
     * Get and update all the custom values inserted by the developer in the {@link SpectreView} on
     * the xml. If no values were inserted, sets the default ones.
     *
     * @param attrs   attribute set with
     * @param context application context
     */
    private void updateAttributeSetValues(AttributeSet attrs, Context context) {
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                    .SpectreView);
            mAttrBarColor = typedArray.getColor(R.styleable.SpectreView_lineColor, 0xff00000);
            mAttrBackgroundColor = typedArray.getColor(R.styleable.SpectreView_backgroundColor,
                    0xff00000);
            mAttrBarCount = typedArray.getInt(R.styleable.SpectreView_barCount, 64);
            mAttrShowSeparator = typedArray.getBoolean(R.styleable.SpectreView_showSeparator, true);
            typedArray.recycle();
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

    @Override
    public void run() {
        while (mRunning) {
            if (!mHolder.getSurface().isValid()) {
                continue;
            }
            try {
                draw();
                long delay = (System.currentTimeMillis() - mCurrentTime) / 1000;
                mCurrentTime = System.currentTimeMillis();
                if (delay < DELAY) {
                    Thread.sleep(DELAY - delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    /**
     * Draw the spectrum view.
     */
    private void draw() {
        final Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mBarWidth = getWidth() / mAttrBarCount;
        mMargin = (getWidth() - mAttrBarCount * mBarWidth) / 2;
        final int centerHeight = getHeight() / 2;

        drawBackground(canvas);
        drawSeparator(canvas, centerHeight);
        drawBars(canvas, centerHeight);

        mHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * Draw the center separator.
     *
     * @param canvas       canvas to be drawn
     * @param centerHeight half height of the separator
     */
    private void drawSeparator(Canvas canvas, int centerHeight) {
        if (mAttrShowSeparator) {
            final Rect separator = new Rect();
            separator.set(
                    mMargin,
                    centerHeight - SEPARATOR_HALF_HEIGHT,
                    mAttrBarCount *
                            mBarWidth + mMargin,
                    centerHeight + SEPARATOR_HALF_HEIGHT);
            canvas.drawRect(separator, getDefaultPaint());
        }
    }

    /**
     * Draw the background based on the color selected.
     *
     * @param canvas canvas to be drawn
     */
    private void drawBackground(Canvas canvas) {
        final Paint backgroundColor = new Paint();
        backgroundColor.setColor(mAttrBackgroundColor);
        backgroundColor.setStyle(Paint.Style.FILL);

        final Rect background = new Rect();
        background.set(mMargin, 0, mAttrBarCount * mBarWidth + mMargin, getHeight());
        canvas.drawRect(background, backgroundColor);
    }

    /**
     * Draw the bar
     *
     * @param canvas       canvas to be drawn
     * @param centerHeight half height of the bar
     */
    private void drawBars(Canvas canvas, int centerHeight) {
        for (int i = 0; i < mVolumeList.size(); i++) {
            final Rect rect = new Rect();
            final int halfVolume = mVolumeList.get(i) * getHeight() / 210;
            final int left = mMargin + i * mBarWidth;
            final int right = left + mBarWidth - 1;
            rect.set(left, centerHeight - halfVolume, right, centerHeight + halfVolume);
            canvas.drawRect(rect, getDefaultPaint());
        }
    }

    /**
     * Get the default paint of the spectrum view.
     *
     * @return default paint
     */
    private Paint getDefaultPaint() {
        final Paint paint = new Paint();
        paint.setColor(mAttrBarColor);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    /**
     * Add a volume to be drawn as a bar in the spectrum view.
     *
     * @param volume volume to be drawn
     */
    public void add(int volume) {
        volume = volume * volume / (100) * volume / (100);
        mVolumeList.add(volume);
        if (mVolumeList.size() > mAttrBarCount) {
            mVolumeList.remove();
        }
    }

    /**
     * Clear the volume list.
     */
    public void clear() {
        mVolumeList.clear();
    }
}
