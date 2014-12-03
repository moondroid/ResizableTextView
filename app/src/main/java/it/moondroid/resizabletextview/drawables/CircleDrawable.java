package it.moondroid.resizabletextview.drawables;

/**
 * Created by marco.granatiero on 19/11/2014.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class CircleDrawable extends BaseDrawable {

    public CircleDrawable(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public void draw(Canvas canvas) {

        int height = getBounds().height();
        int width = getBounds().width();

        float mid = width / 2;
        float min = Math.min(width, height);
        float half = min / 2;
        float rad = half;
        mid = mid - half;


        canvas.drawCircle(mid + half , half, rad, paint);

    }


}