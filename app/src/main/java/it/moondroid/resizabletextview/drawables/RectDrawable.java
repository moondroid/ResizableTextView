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

public class RectDrawable extends Drawable {

    private Paint paint;

    public RectDrawable(Bitmap bitmap) {
//        BitmapShader shader;
//        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
//                Shader.TileMode.CLAMP);
//        paint.setShader(shader);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

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

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}