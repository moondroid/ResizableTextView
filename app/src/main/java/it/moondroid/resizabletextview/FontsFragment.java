package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class FontsFragment extends Fragment implements IEffectFragment, AdapterView.OnItemClickListener {

    private static final String KEY_FONT_ID = "FontsFragment.KEY_FONT_ID";
    private HListView mListView;
    private OnFontSelectedListener mListener;
    private ResizableTextView mResizableTextView;

    public interface OnFontSelectedListener {
        public void onFontSelected(int fontId, Typeface typeface);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFontSelectedListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString() + " must implement OnFontSelectedListener");
            Log.w("FontsFragment", activity.toString() + " must implement OnFontSelectedListener");
        }
    }

    @Override
    public void setResizableItem(ResizableLayout resizableTextView) {
        mResizableTextView = (ResizableTextView) resizableTextView;
    }

    public static FontsFragment newInstance(int fontId){
        FontsFragment f = new FontsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_FONT_ID, fontId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fonts_fragment, container, false);

        mListView = (HListView)view.findViewById(R.id.hListFont);
        List<String> items = new ArrayList<String>();
        for( int i = 0; i < Assets.fonts.size(); i++ ) {
            items.add("Aa");
        }
        //mAdapter = new TestAdapter( this, R.layout.test_item_1, android.R.id.text1, items );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_font, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_font, parent, false);
                TextView textView = (TextView) rowView.findViewById(R.id.text);
                textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Assets.fonts.get(position)));

                return rowView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //int item = getArguments().getInt(KEY_FONT_ID, 0);
        int item = mResizableTextView.getFontId();
        mListView.setItemChecked(item, true);
        mListView.smoothScrollToPosition(item);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListView.setItemChecked(i, true);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), Assets.fonts.get(i));
        mResizableTextView.setFontId(i);
        mListener.onFontSelected(i, typeface);
    }
}
