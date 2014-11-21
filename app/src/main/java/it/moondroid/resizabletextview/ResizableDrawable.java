package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableDrawable extends ResizableLayout implements IEffectable {

    private static final int DRAWABLE_MIN_SIZE = 100;
    private static final int DRAWABLE_DEFAULT_SIZE = 200;

    private Context context;
    private ImageView imageView;
    private int drawableId;

    public ResizableDrawable(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected View getResizableView(Context context) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setDrawableId(0);

        return imageView;
    }

    @Override
    protected void setViewSize(int size) {
        LayoutParams params = (LayoutParams) imageView.getLayoutParams();
        params.height = size;
        params.width = size;
        imageView.setLayoutParams(params);
    }

    @Override
    protected int getViewSize() {
        return imageView.getLayoutParams().width;
    }

    @Override
    protected int getMinViewSize() {
        return DRAWABLE_MIN_SIZE;
    }

    @Override
    protected int getDefaultViewSize() {
        return DRAWABLE_DEFAULT_SIZE;
    }

    @Override
    public int getColorId() {
        return 0;
    }

    @Override
    public void setColorId(int colorId) {

    }

    @Override
    public int getFontId() {
        return 0;
    }

    @Override
    public void setFontId(int fontId) {

    }

    @Override
    public int getDrawableId(){
        return drawableId;
    }

    @Override
    public void setDrawableId(int drawableId){

        Drawable drawable = Assets.getDrawable(drawableId, DRAWABLE_DEFAULT_SIZE, DRAWABLE_DEFAULT_SIZE);
        if(drawable != null){
            imageView.setImageDrawable(drawable);
        }

        this.drawableId = drawableId;
    }

    @Override
    public int getStickerId() {
        return 0;
    }

    @Override
    public void setStickerId(int stickerId) {

    }
}
