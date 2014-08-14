package com.hp.android.yamba;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class TimelineFragment extends Fragment {

    public interface OnTimelineInteractionListener {
        public void onTimelineItemClick(Uri uri);
    }

    private OnTimelineInteractionListener mListener;

    private ListView mListView;
    private TextView mEmptyView;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_timeline, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mEmptyView = (TextView) root.findViewById(R.id.empty);

        mListView.setEmptyView(mEmptyView);

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTimelineItemClick(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTimelineInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
