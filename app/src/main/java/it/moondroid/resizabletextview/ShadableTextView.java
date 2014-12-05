package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.widget.TextView;

import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 05/12/2014.
 */
public class ShadableTextView extends TextView {

    protected Shadow shadow;
    protected Shader shader;

    public ShadableTextView(Context context) {
        super(context);
    }

    @Override
    public void draw(Canvas canvas) {
        getPaint().setShader(null);
        getPaint().setShadowLayer(shadow.radius, shadow.dx, shadow.dy, shadow.color);
        super.draw(canvas);

        getPaint().setShader(shader);
        getPaint().clearShadowLayer();
        super.draw(canvas);
    }


    public void setShadow(Shadow shadow){
        this.shadow = shadow;
    }

    public void setPattern(String newPattern){

        Bitmap patternBMP = Assets.getBitmapFromAsset(getContext(), newPattern);
        shader = new BitmapShader(patternBMP,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }


}
