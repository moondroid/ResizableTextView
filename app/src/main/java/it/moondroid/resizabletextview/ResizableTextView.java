package it.moondroid.resizabletextview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableTextView extends FrameLayout {

    private static final float MIN_SIZE = 20.0f;
    private static final boolean SNAP_ROTATION = true;

    private TextView textView;
    private View resizeView, rotateView, removeView, editView;
    private Point pivot;
    private boolean isEditingEnabled;

    private OnResizableTextViewListener listener = new OnResizableTextViewListener() {
        @Override
        public void onTranslationChanged(ResizableTextView view, float translationX, float translationY) {
            //do nothing
        }

        @Override
        public void onSizeChanged(ResizableTextView view, float size) {
            //do nothing
        }

        @Override
        public void onRotationChanged(ResizableTextView view, float rotation) {
            //do nothing
        }

        @Override
        public void onRemove(ResizableTextView view) {
            //do nothing
        }

        public void onEdit(ResizableTextView view){
            //do nothing
        }
    };


    public interface OnResizableTextViewListener {
        public void onTranslationChanged (ResizableTextView view, float translationX, float translationY);
        public void onSizeChanged(ResizableTextView view, float size);
        public void onRotationChanged(ResizableTextView view, float rotation);
        public void onRemove(ResizableTextView view);
        public void onEdit(ResizableTextView view);
    }

    public ResizableTextView(Context context) {
        super(context);
        setup(context);
    }

    public ResizableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ResizableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    private void setup(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.resizable_textview, this);
        setOnTouchListener(translateTouchListener);

        rotateView = findViewById(R.id.rotate_view);
        rotateView.setOnTouchListener(rotateTouchListener);
        resizeView = findViewById(R.id.size_view);
        resizeView.setOnTouchListener(sizeTouchListener);
        removeView = findViewById(R.id.remove_view);
        removeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemove(ResizableTextView.this);
            }
        });
        editView = findViewById(R.id.edit_view);
        editView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(ResizableTextView.this);
            }
        });

        textView = (TextView)findViewById(R.id.textview);
        if(textView.getTextSize()<MIN_SIZE){
            textView.setTextSize(MIN_SIZE);
        }

        setEditingEnabled(true);//editing enabled by default

    }

    public void setEditingEnabled(boolean enabled){
        if(enabled){
            rotateView.setVisibility(VISIBLE);
            resizeView.setVisibility(VISIBLE);
            removeView.setVisibility(VISIBLE);
            editView.setVisibility(VISIBLE);
            textView.setBackgroundResource(R.drawable.textview_border);
        }else {
            rotateView.setVisibility(GONE);
            resizeView.setVisibility(GONE);
            removeView.setVisibility(GONE);
            editView.setVisibility(GONE);
            textView.setBackgroundResource(android.R.color.transparent);
        }
        isEditingEnabled = enabled;
    }

    public boolean isEditingEnabled(){
        return isEditingEnabled;
    }

    public void setOnResizableTextViewListener(OnResizableTextViewListener listener){
        if (listener != null) {
            this.listener = listener;
        }
    }


    public TextView getTextView(){
        return textView;
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
                if(!isEditingEnabled){
                    setEditingEnabled(true);
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                int tapTimeout = ViewConfiguration.get(ResizableTextView.this.getContext()).getTapTimeout();
                int longPressTimeout = ViewConfiguration.get(ResizableTextView.this.getContext()).getLongPressTimeout();
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
                listener.onTranslationChanged(ResizableTextView.this, newTranslationX, newTranslationY);
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
                    startScale = textView.getTextSize();
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
                if (endScale < MIN_SIZE) {
                    endScale = MIN_SIZE;
                }
                listener.onSizeChanged(ResizableTextView.this, (float)endScale);
                textView.setTextSize((float)endScale);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("ResizableTextView.onTouch ACTION_UP", "endScale = " + endScale);
                startScale = endScale;
                findViewById(R.id.size_view).setSelected(false);
            }

            return true;
        }
    };

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
                listener.onRotationChanged(ResizableTextView.this, rotation);

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
}
