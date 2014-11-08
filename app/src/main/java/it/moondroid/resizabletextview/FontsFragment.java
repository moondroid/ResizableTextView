package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class FontsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> fonts = new ArrayList<String>();
    OnFontSelectedListener mListener;

    public interface OnFontSelectedListener {
        public void onFontSelected(Typeface typeface);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFontSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFontSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fonts_fragment, container, false);

//        for( int i = 1; i < 30; i++ ) {
//            String fontPath = "fonts/simple_"+String.format("%03d", i)+".ttf";
//            Log.d("FontsFragment", fontPath);
//            fonts.add( fontPath );
//        }
        fonts.add("fonts/simple_001.ttf");
        fonts.add("fonts/simple_002.ttf");
        fonts.add("fonts/simple_006.ttf");
        fonts.add("fonts/simple_007.ttf");
        fonts.add("fonts/simple_008.ttf");
        fonts.add("fonts/simple_010.ttf");
        fonts.add("fonts/simple_011.ttf");
        fonts.add("fonts/simple_012.ttf");
        fonts.add("fonts/simple_013.ttf");
        fonts.add("fonts/simple_015.ttf");
        fonts.add("fonts/simple_016.ttf");
        fonts.add("fonts/simple_018.ttf");
        fonts.add("fonts/simple_019.ttf");
        fonts.add("fonts/simple_020.ttf");
        fonts.add("fonts/simple_029.ttf");

        HListView listView = (HListView)view.findViewById(R.id.hListFont);
        List<String> items = new ArrayList<String>();
        for( int i = 0; i < fonts.size(); i++ ) {
            items.add("Aa");
        }
        //mAdapter = new TestAdapter( this, R.layout.test_item_1, android.R.id.text1, items );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_font, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView)super.getView(position, convertView, parent);
                textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), fonts.get(position)));

                return textView;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), fonts.get(i));
        mListener.onFontSelected(typeface);
    }
}
