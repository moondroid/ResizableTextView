package it.moondroid.resizabletextview.drawables;

/**
 * Created by marco.granatiero on 19/11/2014.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class RectDrawable extends BaseDrawable {


    public RectDrawable(Bitmap bitmap) {
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

        //canvas.drawRect(0, 0, width, height, paint);
        int radius = 10; // note this is actual pixels
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,  paint);
    }


}