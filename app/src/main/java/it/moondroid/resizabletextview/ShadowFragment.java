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
public class ShadowFragment extends Fragment implements IEffectFragment {

    private OnDrawableSelectedListener mListener;
    private IEffectable mEffectableItem;
    private Shadow mShadow;

    public interface OnDrawableSelectedListener {
        public void onDrawableSelected(int drawableId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDrawableSelectedListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString() + " must implement OnStickerSelectedListener");
            Log.w("DrawablesFragment", activity.toString() + " must implement OnDrawableSelectedListener");
            mListener = new OnDrawableSelectedListener() {
                @Override
                public void onDrawableSelected(int stickerId) {
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
        seekBarRadius.setProgress((int) mShadow.radius);

        seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mShadow.radius = seekBar.getProgress();
                    mEffectableItem.setShadow(mShadow);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }



}
