package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.InvocationTargetException;

import it.moondroid.resizabletextview.drawables.BaseDrawable;
import it.moondroid.resizabletextview.drawables.CircleDrawable;
import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableDrawable extends ResizableLayout {

    private static final int DRAWABLE_MIN_SIZE = 100;
    private static final int DRAWABLE_DEFAULT_SIZE = 200;

    private Context context;
    private ImageView imageView;
    private BaseDrawable drawable;
    private int drawableId;
    private int drawableSize;
    private Shadow shadow = new Shadow();
    private int shadowWidth = 0;
    private int shadowHeight = 0;
    private int colorId;

    public ResizableDrawable(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.context = context;
    }


    @Override
    protected View getResizableView(Context context) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
//        imageView.setPadding(0, 0, 10, 10);
        setDrawableId(0);

        return imageView;
    }

    @Override
    protected void setViewSize(int size) {
        drawableSize = size;
        LayoutParams params = (LayoutParams) imageView.getLayoutParams();
        params.height = size + shadowHeight;
        params.width = size + shadowWidth;
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

        drawable = (BaseDrawable) Assets.getDrawable(drawableId, DRAWABLE_DEFAULT_SIZE, DRAWABLE_DEFAULT_SIZE);
        if(drawable != null){
            imageView.setImageDrawable(drawable);
            setShadow(shadow == null? new Shadow() : shadow);
        }
        this.drawableSize = getViewSize();
        this.drawableId = drawableId;

    }

    @Override
    public int getStickerId() {
        return 0;
    }

    @Override
    public void setStickerId(int stickerId) {

    }

    @Override
    public void setShadow(Shadow shadow) {
        this.shadow = shadow;

        drawable.getPaint().setShadowLayer(shadow.radius, shadow.dx, shadow.dy, shadow.color);

        imageView.setPadding((int)(shadow.radius+shadow.dx), (int)(shadow.radius+shadow.dy),
                (int)(shadow.radius+shadow.dx), (int)(shadow.radius+shadow.dy));
        shadowWidth = (int) ((shadow.radius + shadow.dx)*2);
        shadowHeight = (int) ((shadow.radius + shadow.dy)*2);
        setViewSize(drawableSize);

    }


    @Override
    public Shadow getShadow() {
        return shadow;
    }
}
