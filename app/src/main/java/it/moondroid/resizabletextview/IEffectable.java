package it.moondroid.resizabletextview;

import it.moondroid.resizabletextview.entities.Shadow;

/**
 * Created by marco.granatiero on 21/11/2014.
 */
public interface IEffectable {

    public int getColorId();
    public void setColorId(int colorId);

    public int getFontId();
    public void setFontId(int fontId);

    public int getDrawableId();
    public void setDrawableId(int drawableId);

    public int getStickerId();
    public void setStickerId(int stickerId);

    public int getPatternId();
    public void setPatternId(int patternId);

    public void setShadow(Shadow shadow);
    public Shadow getShadow();
}
