package it.moondroid.resizabletextview;

import android.content.Context;
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
import android.widget.TextView;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableTextView extends ResizableLayout {

    private static final int TEXT_MIN_SIZE = 20;
    private static final int TEXT_DEFAULT_SIZE = 60;

    private int fontId;
    private int colorId;

    private Context context;
    private TextView textView;

    public ResizableTextView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected View getResizableView(Context context) {
        textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Hello");

        return textView;
    }

    @Override
    protected void setViewSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    protected int getViewSize() {
        return (int) textView.getTextSize();
    }

    @Override
    protected int getMinViewSize() {
        return TEXT_MIN_SIZE;
    }

    @Override
    protected int getDefaultViewSize() {
        return TEXT_DEFAULT_SIZE;
    }

    public void setFontId(int fontId){
        if(fontId>=0 && fontId<Assets.fonts.size()){
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), Assets.fonts.get(fontId));
            textView.setTypeface(typeface);
            this.fontId = fontId;
        }
    }

    public int getFontId(){
        return fontId;
    }

    public void setColorId(int colorId){
        if(colorId>=0 && colorId<Assets.colors.size()){
            int color = Assets.colors.get(colorId);
            textView.setTextColor(color);
            this.colorId = colorId;
        }
    }

    public int getColorId(){
        return colorId;
    }

    public TextView getTextView(){
        return textView;
    }

}
