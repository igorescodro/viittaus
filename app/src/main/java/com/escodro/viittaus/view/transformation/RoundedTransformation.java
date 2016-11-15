package com.escodro.viittaus.view.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Custom {@link Picasso} transformation to draw the pictures in rounded shape.
 * <p>
 * Created by Igor Escodro on 13/5/2016.
 */
public class RoundedTransformation implements Transformation {

    /**
     * Key to identify the {@link Transformation}.
     */
    private static final String TRANSFORMATION_KEY = "rounded";

    /**
     * {@inheritDoc}
     * <p/>
     * <b>Override:</b><br>Transform the default bitmap in rounded shape bitmap.
     *
     * @param source bitmap to be transformed
     *
     * @return bitmap in rounded shape
     */
    @Override
    public Bitmap transform(final Bitmap source) {
        final int size = Math.min(source.getWidth(), source.getHeight());

        final int x = (source.getWidth() - size) / 2;
        final int y = (source.getHeight() - size) / 2;

        final Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        final Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        final BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        final float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return TRANSFORMATION_KEY;
    }
}
