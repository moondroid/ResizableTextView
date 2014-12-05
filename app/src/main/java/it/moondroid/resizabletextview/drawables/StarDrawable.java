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
import android.graphics.drawable.Drawable;

public class StarDrawable extends BaseDrawable {

    private Path path;

    public StarDrawable(Bitmap bitmap) {
        super(bitmap);
        path = new Path();
    }

    @Override
    protected void drawShape(Canvas canvas, Paint paint){
        int height = getBounds().height();
        int width = getBounds().width();

        float mid = width / 2;
        float min = Math.min(width, height);
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;

        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(mid + half, half, rad, paint);

        path.reset();

        paint.setStyle(Paint.Style.FILL);


        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f);

        path.close();
        canvas.drawPath(path, paint);

    }


}