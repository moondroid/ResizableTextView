package it.moondroid.resizabletextview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

/**
 * Created by marco.granatiero on 05/11/2014.
 */
public class ResizableTextViewOld extends TextView {

    private static final float SCALE_GESTURE_SPEED = 1.2f;
    private float mScaleFactor = 1.0f;
    private float mStartSize;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //do nothing
        }
    };

    public ResizableTextViewOld(Context context) {
        this(context, null);
    }

    public ResizableTextViewOld(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizableTextViewOld(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mGestureDetector = new GestureDetector(getContext(), new RadarGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new TextViewOnScaleGestureListener());

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            mScaleGestureDetector.setQuickScaleEnabled(true);
        }

        mStartSize = getTextSize();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l!=null){
            onClickListener = l;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        boolean result = mScaleGestureDetector.onTouchEvent(event);
        Log.d("ResizableTextView.onTouchEvent", "result: "+result);

        if (!mScaleGestureDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }

//        if (result){
//            return false;
//        }else {
//            return super.onTouchEvent(event);
//        }
        return true;
    }

    private class TextViewOnScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor *= detector.getScaleFactor()*SCALE_GESTURE_SPEED;

            // Don't let the object get too small or too large.
            //mScaleFactor = Math.max(30.0f, Math.min(mScaleFactor, 300.0f));
            mScaleFactor = Math.min(mScaleFactor, 300.0f);
            Log.d("ResizableTextView.onScale", "mScaleFactor: "+mScaleFactor);

            float span = detector.getCurrentSpan();
            float currentSize = getHeight();
            float scale = span/currentSize;
            Log.d("ResizableTextView.onScale", "scale: "+scale);

//            ResizableTextView.this.setTextSize(mStartSize + mScaleFactor);
            ResizableTextViewOld.this.setTextSize(getTextSize() * scale);
            invalidate();



//
//            float speed;
//            if (span > 0) {
//                speed = SCALE_GESTURE_SPEED;
//            } else {
//                speed = 1.0f / SCALE_GESTURE_SPEED;
//            }
//            if (span != 0) {
//                float targetScale = ResizableTextView.this.getTextSize() / (detector.getScaleFactor() * speed);
//                //targetScale = Math.min(RADAR_DISTANCE_MAX, Math.max(targetScale, RADAR_DISTANCE_MIN));
//                //ResizableTextView.this.setTextSize(targetScale);
//                ResizableTextView.this.setTextSize(ResizableTextView.this.getTextSize() * detector.getScaleFactor());
//
//                invalidate();
//            }

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    private class RadarGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            onClickListener.onClick(ResizableTextViewOld.this);
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
