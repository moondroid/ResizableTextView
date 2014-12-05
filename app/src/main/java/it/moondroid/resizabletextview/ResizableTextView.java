package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableTextView extends ResizableLayout implements IEffectable {

    private static final String TEXT_DEFAULT = "Hello";
    private static final int TEXT_MIN_SIZE = 20;
    private static final int TEXT_DEFAULT_SIZE = 60;
    private static final int DEFAULT_COLOR_ID = 0;
    private static final int DEFAULT_FONT_ID = 0;

    private int fontId;
    private int colorId;
    private int textSize;
    private int patternId;

    private Context context;
    private ShadableTextView textView;

    private Shadow shadow = new Shadow();
    private int shadowWidth = 0;
    private int shadowHeight = 0;

    public ResizableTextView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected View getResizableView(Context context) {
        //textView = new CustomTextView(context);
        textView = new ShadableTextView(context);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(TEXT_DEFAULT);

        setColorId(DEFAULT_COLOR_ID);
        setFontId(DEFAULT_FONT_ID);

        return textView;
    }

    @Override
    protected void setViewSize(int size) {
        textSize = size;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size );
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

    @Override
    public void setFontId(int fontId){
        if(fontId>=0 && fontId<Assets.fonts.size()){
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), Assets.fonts.get(fontId));
            textView.setTypeface(typeface);
            setShadow(shadow == null? new Shadow() : shadow);
            this.fontId = fontId;
            this.textSize = getViewSize();
        }
    }

    @Override
    public int getDrawableId() {
        return 0;
    }

    @Override
    public void setDrawableId(int drawableId) {

    }

    @Override
    public int getStickerId() {
        return 0;
    }

    @Override
    public void setStickerId(int stickerId) {

    }

    @Override
    public int getFontId(){
        return fontId;
    }

    @Override
    public void setColorId(int colorId){
        if(colorId>=0 && colorId<Assets.colors.size()){
            //int color = Assets.colors.get(colorId);
            int color = getResources().getColor(Assets.colors.get(colorId));
            textView.setTextColor(color);
            this.colorId = colorId;
        }
    }

    @Override
    public int getColorId(){
        return colorId;
    }

    public TextView getTextView(){
        return textView;
    }


    @Override
    public int getPatternId() {
        return patternId;
    }

    @Override
    public void setPatternId(int patternId) {

//        Bitmap patternBMP = Assets.getBitmapFromAsset(context, Assets.patterns.get(patternId));
//        //create shader
//        BitmapShader patternBMPshader = new BitmapShader(patternBMP,
//                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//
//        //color and shader
//        textView.getPaint().setColor(0xFFFFFFFF);
//        textView.getPaint().setShader(patternBMPshader);

        textView.setPattern(Assets.patterns.get(patternId));
        textView.invalidate();
    }

    @Override
    public void setShadow(Shadow shadow) {
        this.shadow = shadow;

        //textView.setShadowLayer(shadow.radius, shadow.dx, shadow.dy, shadow.color);
        textView.setShadow(shadow);

        textView.setPadding((int)(shadow.radius+shadow.dx), (int)(shadow.radius+shadow.dy),
                (int)(shadow.radius+shadow.dx), (int)(shadow.radius+shadow.dy));
        shadowWidth = (int) ((shadow.radius + shadow.dx)*2);
        shadowHeight = (int) ((shadow.radius + shadow.dy)*2);
        setViewSize(textSize);
    }


    @Override
    public Shadow getShadow() {
        return shadow;
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
