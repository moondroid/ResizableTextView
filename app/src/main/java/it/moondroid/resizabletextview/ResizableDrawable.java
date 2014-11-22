package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableDrawable extends ResizableLayout {

    private static final int DRAWABLE_MIN_SIZE = 100;
    private static final int DRAWABLE_DEFAULT_SIZE = 200;

    private Context context;
    private ImageView imageView;

    private int colorId;

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
        return colorId;
    }

    @Override
    public void setColorId(int colorId) {
        if(colorId>=0 && colorId<Assets.colors.size()){
            int color = getResources().getColor(Assets.colors.get(colorId));
            imageView.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            this.colorId = colorId;
        }
    }

    @Override
    public int getDrawableId(){
        return drawableId;
    }

    @Override
    public void setDrawableId(int drawableId){

        int drawableSize = DRAWABLE_DEFAULT_SIZE;
        if(getMeasuredWidth()!=0 && getMeasuredHeight()!=0){
            drawableSize = getMeasuredWidth();
        }

        Drawable drawable = Assets.getDrawable(drawableId, drawableSize, drawableSize);
        if(drawable != null){
            imageView.setImageDrawable(drawable);

            Bitmap bitmap = Assets.getBitmapFromDrawable(drawable, drawableSize, drawableSize);
            if (bitmap!=null){
                imageView.setImageBitmap(Assets.addShadow(bitmap));
            }

        }


        this.drawableId = drawableId;
    }


}
