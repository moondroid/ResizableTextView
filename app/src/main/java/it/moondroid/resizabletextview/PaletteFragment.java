package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
public class PaletteFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String KEY_COLOR_ID = "PaletteFragment.KEY_COLOR_ID";
    private HListView mListView;
    private OnColorSelectedListener mListener;

    public interface OnColorSelectedListener {
        public void onColorSelected(int colorId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFontSelectedListener");
        }
    }

    public static PaletteFragment newInstance(int colorId){
        PaletteFragment f = new PaletteFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_COLOR_ID, colorId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fonts_fragment, container, false);//TODO change layout name to generic name

        mListView = (HListView)view.findViewById(R.id.hListFont);
//        List<Integer> items = new ArrayList<Integer>();
//        for( int i = 0; i < Assets.fonts.size(); i++ ) {
//            items.add("Aa");
//        }
        List<Integer> items = Assets.colors;
        //mAdapter = new TestAdapter( this, R.layout.test_item_1, android.R.id.text1, items );
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), R.layout.item_color, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_color, parent, false);
                View colorView = rowView.findViewById(R.id.color);
                colorView.setBackgroundColor(Assets.colors.get(position));
                return rowView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        int item = getArguments().getInt(KEY_COLOR_ID, 0);
        mListView.setItemChecked(item, true);
        mListView.smoothScrollToPosition(item);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListView.setItemChecked(i, true);
        mListener.onColorSelected(i);
    }
}
