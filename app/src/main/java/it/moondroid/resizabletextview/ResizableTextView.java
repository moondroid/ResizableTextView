package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
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
        //textView = new CustomTextView(context);
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

    private class CustomTextView extends TextView {
        private static final String YOUR_TEXT = "something cool";
        private Path _arc;
        private Paint _paintText;
        private int textHeight;

        public CustomTextView(Context context) {
            super(context);

//            _arc = new Path();
//            //RectF oval = new RectF(50,100,200,250);
//            RectF oval = new RectF(0, 0, getMeasuredWidth() ,getMeasuredHeight());
//            _arc.addArc(oval, -180, 200);
//            _paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
//            _paintText.setStyle(Paint.Style.FILL_AND_STROKE);
//            _paintText.setColor(Color.WHITE);
//            _paintText.setTextSize(20f);

//            Rect result = new Rect();
//            getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), result);
//            textHeight = result.height();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            _arc = new Path();

            RectF oval = new RectF(0, 0, getMeasuredWidth() ,getMeasuredHeight());
            _arc.addArc(oval, -180, 200);
            Paint pathPaint = new Paint(Color.GREEN);
            pathPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(_arc, pathPaint);

            canvas.drawTextOnPath(getText().toString(), _arc, 0, getMeasuredHeight()/2, getPaint());
//            invalidate();
        }
    }
}
