package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableImageView extends ResizableLayout implements IEffectable {

    private static final int IMAGE_MIN_SIZE = 100;
    private static final int IMAGE_DEFAULT_SIZE = 200;

    private Context context;
    private ImageView imageView;
    private int stickerId;

    public ResizableImageView(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected View getResizableView(Context context) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
//        stickerId = 0;
//        imageView.setImageResource(Assets.stickers.get(stickerId));
        setStickerId(0);

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
        return IMAGE_MIN_SIZE;
    }

    @Override
    protected int getDefaultViewSize() {
        return IMAGE_DEFAULT_SIZE;
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
    public int getDrawableId() {
        return 0;
    }

    @Override
    public void setDrawableId(int drawableId) {

    }

    @Override
    public int getStickerId(){
        return stickerId;
    }

    @Override
    public void setStickerId(int stickerId){
//        imageView.setImageResource(Assets.stickers.get(stickerId));
        Bitmap bitmap = Assets.getBitmapFromAsset(getContext(), Assets.stickers.get(stickerId));
        if (bitmap!=null){

            imageView.setImageBitmap(Assets.addShadow(bitmap));
        }
        this.stickerId = stickerId;
    }
}
