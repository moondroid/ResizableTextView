package it.moondroid.resizabletextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 21/11/2014.
 */
public class EffectableImageView extends ImageView implements IEffectable {

    private int colorId;

    public EffectableImageView(Context context) {
        super(context);
    }

    public EffectableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EffectableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getColorId() {
        return colorId;
    }

    @Override
    public void setColorId(int colorId) {
        if(colorId>=0 && colorId<Assets.colors.size()){
            //int color = Assets.colors.get(colorId);
            int color = getResources().getColor(Assets.colors.get(colorId));
            setBackgroundColor(color);
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
    public Shadow getShadow() {
        return new Shadow();
    }
}
