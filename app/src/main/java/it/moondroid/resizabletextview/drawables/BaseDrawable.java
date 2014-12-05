package it.moondroid.resizabletextview.drawables;

/**
 * Created by marco.granatiero on 19/11/2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import it.moondroid.resizabletextview.Assets;
import it.moondroid.resizabletextview.entities.Shadow;

public abstract class BaseDrawable extends Drawable {

    protected Paint paint;
    protected Shadow shadow;
    protected Shader shader;

    public BaseDrawable(Bitmap bitmap) {

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        drawShape(canvas, getShadowPaint());
        drawShape(canvas, getShaderPaint());
    }

    abstract protected void drawShape(Canvas canvas, Paint paint);

    protected Paint getPaint(){
        return paint;
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

    public void setShadow(Shadow shadow){
        this.shadow = shadow;
    }

    //set pattern
    public void setPattern(Context context, String newPattern){

//        //get pattern
//        int patternID = context.getResources().getIdentifier(newPattern, "drawable", "it.moondroid.resizabletextview");
//        //decode
//        Bitmap patternBMP = BitmapFactory.decodeResource(context.getResources(), patternID);

        Bitmap patternBMP = Assets.getBitmapFromAsset(context, newPattern);
                //create shader
        shader = new BitmapShader(patternBMP,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    protected Paint getShadowPaint(){
        if (shadow != null){
            paint.setShader(null);
            paint.setShadowLayer(shadow.radius, shadow.dx, shadow.dy, shadow.color);
        }
        return paint;
    }

    protected Paint getShaderPaint(){
        if (shader != null){
            paint.setShader(shader);
            paint.clearShadowLayer();
        }
        return paint;
    }
}