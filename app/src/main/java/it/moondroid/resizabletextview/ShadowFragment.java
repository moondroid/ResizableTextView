package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.List;

import it.moondroid.resizabletextview.entities.Shadow;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class ShadowFragment extends Fragment implements IEffectFragment, SeekBar.OnSeekBarChangeListener {

    private OnShadowSelectedListener mListener;
    private IEffectable mEffectableItem;
    private Shadow mShadow;

    public interface OnShadowSelectedListener {
        public void onCreated(int height);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnShadowSelectedListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString() + " must implement OnStickerSelectedListener");
            Log.w("DrawablesFragment", activity.toString() + " must implement OnShadowSelectedListener");
            mListener = new OnShadowSelectedListener() {
                @Override
                public void onCreated(int height) {
                    //do nothing
                }
            };
        }

    }

    @Override
    public void setEffectableItem(IEffectable effectableItem) {
        mEffectableItem = effectableItem;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shadow, container, false);

        mShadow = mEffectableItem.getShadow();
        SeekBar seekBarRadius = (SeekBar)view.findViewById(R.id.seekBarRadius);
        SeekBar seekBarDx = (SeekBar)view.findViewById(R.id.seekBarDx);
        SeekBar seekBarDy = (SeekBar)view.findViewById(R.id.seekBarDy);
        seekBarRadius.setProgress((int) mShadow.radius);
        seekBarDx.setProgress((int) mShadow.dx);
        seekBarDy.setProgress((int) mShadow.dy);

        seekBarRadius.setOnSeekBarChangeListener(this);
        seekBarDx.setOnSeekBarChangeListener(this);
        seekBarDy.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            switch (seekBar.getId()){
                case R.id.seekBarRadius:
                    mShadow.radius = seekBar.getProgress();
                    break;
                case R.id.seekBarDx:
                    mShadow.dx = seekBar.getProgress();
                    break;
                case R.id.seekBarDy:
                    mShadow.dy = seekBar.getProgress();
                    break;
            }

            mEffectableItem.setShadow(mShadow);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
