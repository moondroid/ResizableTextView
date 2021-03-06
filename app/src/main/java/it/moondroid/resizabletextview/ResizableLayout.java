package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public abstract class ResizableLayout extends FrameLayout implements IEffectable {

    private static final boolean SNAP_ROTATION = true;

    private FrameLayout viewContainer;
    private View resizeView, rotateView, removeView, editView;
    private Point pivot;
    private boolean isEditingEnabled;

    protected Integer width = null;
    protected Integer height = null;


    private OnResizableLayoutListener listener = new OnResizableLayoutListener() {

        @Override
        public void onTouched(ResizableLayout view){
            //do nothing
        }

        @Override
        public void onTranslationChanged(ResizableLayout view, float translationX, float translationY) {
            //do nothing
        }

        @Override
        public void onSizeChanged(ResizableLayout view, float size) {
            //do nothing
        }

        @Override
        public void onRotationChanged(ResizableLayout view, float rotation) {
            //do nothing
        }

        @Override
        public void onRemove(ResizableLayout view) {
            //do nothing
        }

        @Override
        public void onEdit(ResizableLayout view){
            //do nothing
        }
    };


    public interface OnResizableLayoutListener {
        public void onTouched(ResizableLayout view);
        public void onTranslationChanged (ResizableLayout view, float translationX, float translationY);
        public void onSizeChanged(ResizableLayout view, float size);
        public void onRotationChanged(ResizableLayout view, float rotation);
        public void onRemove(ResizableLayout view);
        public void onEdit(ResizableLayout view);
    }

    public ResizableLayout(Context context) {
        super(context);
        setup(context);
    }

    public ResizableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ResizableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.resizable_layout, this);
        setOnTouchListener(translateTouchListener);

        rotateView = findViewById(R.id.rotate_view);
        rotateView.setOnTouchListener(rotateTouchListener);
        resizeView = findViewById(R.id.size_view);
        resizeView.setOnTouchListener(sizeTouchListener);
        removeView = findViewById(R.id.remove_view);
        removeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemove(ResizableLayout.this);
            }
        });
        editView = findViewById(R.id.edit_view);
        editView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(ResizableLayout.this);
            }
        });

        viewContainer = (FrameLayout)findViewById(R.id.view_container);
        viewContainer.addView(getResizableView(context));

        setViewSize(getDefaultViewSize());

        Log.d("ResizableTextView.setup", "viewContainer.getTextSize() = " + viewContainer.getMeasuredWidth());

        setEditingEnabled(true);//editing enabled by default

    }

    protected abstract View getResizableView(Context context);

    protected abstract void setViewSize(int size);

    protected abstract int getViewSize();

    protected abstract int getMinViewSize();

    protected abstract int getDefaultViewSize();

    public void setEditingEnabled(boolean enabled){
        if(enabled){
            rotateView.setVisibility(VISIBLE);
            resizeView.setVisibility(VISIBLE);
            removeView.setVisibility(VISIBLE);
            editView.setVisibility(VISIBLE);
            viewContainer.setBackgroundResource(R.drawable.resizable_layout_background);
        }else {
            rotateView.setVisibility(GONE);
            resizeView.setVisibility(GONE);
            removeView.setVisibility(GONE);
            editView.setVisibility(GONE);
            viewContainer.setBackgroundResource(android.R.color.transparent);
        }
        isEditingEnabled = enabled;
    }

    public boolean isEditingEnabled(){
        return isEditingEnabled;
    }

    public void setOnResizableLayoutListener(OnResizableLayoutListener listener){
        if (listener != null) {
            this.listener = listener;
        }
    }


    public FrameLayout getViewContainer(){
        return viewContainer;
    }


    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {

        if (pivot == null) {
            int[] locationOnScreen = new int[2];
            getLocationOnScreen(locationOnScreen);
            pivot = new Point(locationOnScreen[0] + xNew / 2, locationOnScreen[1] + yNew / 2);
        }
        super.onSizeChanged(xNew, yNew, xOld, yOld);
    }

    OnTouchListener translateTouchListener = new OnTouchListener() {
        private float translationX;
        private float translationY;
        private float startX;
        private float startY;

        private long downTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                translationX = getTranslationX();
                translationY = getTranslationY();
                startX = event.getRawX();
                startY = event.getRawY();
                downTime = System.currentTimeMillis();
                listener.onTouched(ResizableLayout.this);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                int tapTimeout = ViewConfiguration.get(ResizableLayout.this.getContext()).getTapTimeout();
                int longPressTimeout = ViewConfiguration.get(ResizableLayout.this.getContext()).getLongPressTimeout();
                if ((System.currentTimeMillis() - downTime) < tapTimeout) {
                    performClick();
                }

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float deltaX = event.getRawX() - startX;
                float deltaY = event.getRawY() - startY;

                float newTranslationX = translationX + deltaX;
                float newTranslationY = translationY + deltaY;

                setTranslationX(newTranslationX);
                setTranslationY(newTranslationY);
                listener.onTranslationChanged(ResizableLayout.this, newTranslationX, newTranslationY);
            }
            return true;
        }
    };

    OnTouchListener sizeTouchListener = new OnTouchListener() {

        private float centerX;
        private float centerY;
        private double startSize;
        private double startScale;
        private double endScale;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                centerX = pivot.x + getTranslationX();
                centerY = pivot.y + getTranslationY();

                float eventX = event.getRawX();
                float eventY = event.getRawY();

                startSize = distance(centerX, centerY, eventX, eventY);

                if (startScale==0){
                    startScale = getViewSize();
                }
                Log.d("ResizableTextView.onTouch ACTION_DOWN", "startScale = " + startScale);

                findViewById(R.id.size_view).setSelected(true);

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                float eventX = event.getRawX();
                float eventY = event.getRawY();

                double finalSize = distance(centerX, centerY, eventX, eventY);
                double ratio = finalSize / startSize;

                endScale = startScale * ratio;
                Log.d("ResizableTextView.onTouch ACTION_MOVE", "endScale = " + endScale);
                if (endScale < getMinViewSize()) {
                    endScale = getMinViewSize();
                }
//                if (endScale > maxWidth){
//                    endScale = maxWidth;
//                }

                width = (int) (endScale);
                height = (int) (endScale);

                listener.onSizeChanged(ResizableLayout.this, (float)endScale);

                setViewSize((int) endScale);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("ResizableTextView.onTouch ACTION_UP", "endScale = " + endScale);
                startScale = endScale;
                findViewById(R.id.size_view).setSelected(false);
            }

            return true;
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if ((width != null) && (height != null)) {
            super.onMeasure(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    OnTouchListener rotateTouchListener = new OnTouchListener() {
        private float startRotation = 0;

        private float centerX;
        private float centerY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                float eventX = event.getRawX();
                float eventY = event.getRawY();

                centerX = pivot.x + getTranslationX();
                centerY = pivot.y + getTranslationY();

                startRotation = getRotation() + getAngle(eventX, eventY, centerX, centerY);

                findViewById(R.id.rotate_view).setSelected(true);

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float eventX = event.getRawX();
                float eventY = event.getRawY();

                float rotation = (startRotation - getAngle(eventX, eventY, centerX, centerY));
                rotation = snapRotation(rotation);

                setRotation(rotation);
                listener.onRotationChanged(ResizableLayout.this, rotation);

            } else if(event.getAction() == MotionEvent.ACTION_UP){
                findViewById(R.id.rotate_view).setSelected(false);
            }
            return true;
        }

        private float getAngle(float _pX, float _pY, float _cx, float _cy) {
            float _Dx = _pX - _cx;
            float _Dy = _pY - _cy;
            double _An;

            if (_Dx != 0) {
                _An = Math.atan((-_Dy) / Math.abs(_Dx)) * 57.2957795130823;
            } else {
                _An = Math.atan((-_Dy) / 1E-8) * 57.2957795130823;
            }
            if (_Dx < 0) {
                _An = 90 + (90 - _An);
            }
            if ((_Dx > 0) && (_Dy > 0)) {
                _An = _An + 360;
            }
            return (float) _An;
        }
    };

    private double distance(double startX, double startY, double endX, double endY) {
        double width = startX - endX;
        double height = startY - endY;
        return Math.sqrt(width * width + height * height);
    }

    private float snapRotation(float rotation){

        if(SNAP_ROTATION){
            float[] snaps = new float[]{0.0f, 90.f, -90.0f, 180.0f, -180.0f};
            for (float snap : snaps){
                if(rotation >= snap-5 && rotation <= snap+5){
                    return  snap;
                }
            }
        }
        return rotation;
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
        return 0;
    }

    @Override
    public void setDrawableId(int drawableId){

    }

    @Override
    public int getStickerId() {
        return 0;
    }

    @Override
    public void setStickerId(int stickerId) {

    }

    @Override
    public int getPatternId() {
        return 0;
    }

    @Override
    public void setPatternId(int patternId) {

    }

    @Override
    public void setShadow(Shadow shadow) {

    }

    @Override
    public Shadow getShadow(){
        return new Shadow();
    }
}
