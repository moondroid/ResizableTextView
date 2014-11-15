package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class StickersFragment extends Fragment implements IEffectFragment, AdapterView.OnItemClickListener {

    private static final String STICKER_ID = "StickersFragment.STICKER_ID";
    private HListView mListView;
    private OnStickerSelectedListener mListener;
    private ResizableImageView mResizableImageView;

    public interface OnStickerSelectedListener {
        public void onStickerSelected(int stickerId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStickerSelectedListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString() + " must implement OnStickerSelectedListener");
            Log.w("StickersFragment", activity.toString() + " must implement OnStickerSelectedListener");
            mListener = new OnStickerSelectedListener() {
                @Override
                public void onStickerSelected(int stickerId) {
                    //do nothing
                }
            };
        }
    }

    @Override
    public void setResizableItem(ResizableLayout resizableTextView) {
        mResizableImageView = (ResizableImageView) resizableTextView;
    }

    public static StickersFragment newInstance(int stickerId){
        StickersFragment f = new StickersFragment();
        Bundle args = new Bundle();
        args.putInt(STICKER_ID, stickerId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_effects, container, false);//TODO change to specific layout

        mListView = (HListView)view.findViewById(R.id.hListEffects);

        List<String> items = Assets.stickers;
        //mAdapter = new TestAdapter( this, R.layout.test_item_1, android.R.id.text1, items );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_sticker, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_sticker, parent, false);
                ImageView imageView = (ImageView)rowView.findViewById(R.id.sticker);
                //imageView.setImageResource(Assets.stickers.get(position));
                Bitmap bitmap = Assets.getBitmapFromAsset(getActivity(), Assets.stickers.get(position));
                if (bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }

                return rowView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //int item = getArguments().getInt(STICKER_ID, 0);
        int item = mResizableImageView.getStickerId();
        mListView.setItemChecked(item, true);
        mListView.smoothScrollToPosition(item);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mListView.setItemChecked(i, true);
        mResizableImageView.setStickerId(i);
        mListener.onStickerSelected(i);
    }
}
