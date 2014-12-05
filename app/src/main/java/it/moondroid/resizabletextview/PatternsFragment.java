package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import it.moondroid.resizabletextview.drawables.BaseDrawable;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class PatternsFragment extends Fragment implements IEffectFragment, AdapterView.OnItemClickListener {

    private static final String PATTERN_ID = "PatternsFragment.DRAWABLE_ID";
    private int DRAWABLE_DEFAULT_SIZE;
    private static final int DRAWABLE_DEFAULT_COLOR = Color.WHITE;
    private HListView mListView;
    private OnPatternSelectedListener mListener;
    private IEffectable mEffectableItem;

    public interface OnPatternSelectedListener {
        public void onPatternSelected(int drawableId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPatternSelectedListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString() + " must implement OnStickerSelectedListener");
            Log.w("DrawablesFragment", activity.toString() + " must implement OnDrawableSelectedListener");
            mListener = new OnPatternSelectedListener() {
                @Override
                public void onPatternSelected(int stickerId) {
                    //do nothing
                }
            };
        }
        DRAWABLE_DEFAULT_SIZE = (int) (32.0f * getResources().getDisplayMetrics().density + 0.5f); //32dp
    }

    @Override
    public void setEffectableItem(IEffectable effectableItem) {
        mEffectableItem = effectableItem;
    }

    public static PatternsFragment newInstance(int drawableId){
        PatternsFragment f = new PatternsFragment();
        Bundle args = new Bundle();
        args.putInt(PATTERN_ID, drawableId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_effects, container, false);//TODO change to specific layout

        mListView = (HListView)view.findViewById(R.id.hListEffects);

        List<String> items = Assets.patterns;
        //mAdapter = new TestAdapter( this, R.layout.test_item_1, android.R.id.text1, items );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_sticker, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_sticker, parent, false);
                ImageView imageView = (ImageView)rowView.findViewById(R.id.sticker);

                BaseDrawable drawable = Assets.getDrawable(0, DRAWABLE_DEFAULT_SIZE, DRAWABLE_DEFAULT_SIZE);
                if(drawable != null){
                    drawable.setColorFilter(DRAWABLE_DEFAULT_COLOR, PorterDuff.Mode.MULTIPLY);
                    drawable.setPattern(getContext(), Assets.patterns.get(position));
                    imageView.setImageDrawable(drawable);
                }

                return rowView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //int item = getArguments().getInt(DRAWABLE_ID, 0);
        int item = mEffectableItem.getPatternId();
        mListView.setItemChecked(item, true);
        mListView.smoothScrollToPosition(item);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListView.setItemChecked(i, true);
        mEffectableItem.setPatternId(i);
        mListener.onPatternSelected(i);
    }

}
