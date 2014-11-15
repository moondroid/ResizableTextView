package it.moondroid.resizabletextview;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by Marco on 08/11/2014.
 */
public class EffectsMenuFragment extends Fragment implements IEffectFragment, AdapterView.OnItemClickListener, FragmentManager.OnBackStackChangedListener {

    private HListView mListView;
    private OnEffectSelectedListener mListener;
    private ResizableLayout mResizableLayout;


    @Override
    public void onBackStackChanged() {
        mListView.setVisibility(View.VISIBLE);
    }

    public interface OnEffectSelectedListener {
        public void onEffectSelected(int effectId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEffectSelectedListener) activity;
        } catch (ClassCastException e) {
            Log.w("EffectsMenuFragment", activity.toString() + " must implement OnEffectSelectedListener");
        }
    }

    @Override
    public void setResizableItem(ResizableLayout resizableTextView) {
        mResizableLayout = resizableTextView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_effects, container, false);

        mListView = (HListView) view.findViewById(R.id.hListEffects);

        List<Assets.Effect> items = Assets.effects;
        ArrayAdapter<Assets.Effect> adapter = new ArrayAdapter<Assets.Effect>(getActivity(), R.layout.item_effect, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.item_effect, parent, false);
                View effectView = rowView.findViewById(R.id.effect);
                Assets.Effect effect = Assets.effects.get(position);
                effectView.setBackgroundResource(effect.iconId);
                return rowView;
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        //mListView.setVisibility(View.VISIBLE);

//        int item = getArguments().getInt(KEY_COLOR_ID, 0);
//        mListView.setItemChecked(item, true);
//        mListView.smoothScrollToPosition(item);

        getFragmentManager().addOnBackStackChangedListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (mListener != null) {
            mListener.onEffectSelected(i);
        }

        mListView.setVisibility(View.GONE);

        try {
            Assets.Effect effect = Assets.effects.get(i);

            Fragment subFragment = (Fragment) effect.fragmentClass.getConstructor().newInstance();
            ((IEffectFragment)subFragment).setResizableItem(mResizableLayout);

            getFragmentManager().beginTransaction()
                    .replace(R.id.effect_container, subFragment).addToBackStack(null).commit();


        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

}
