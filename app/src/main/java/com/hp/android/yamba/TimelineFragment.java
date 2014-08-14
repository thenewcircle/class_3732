package com.hp.android.yamba;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TimelineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    public interface OnTimelineInteractionListener {
        public void onTimelineItemClick(Uri uri);
    }

    private OnTimelineInteractionListener mListener;

    private ListView mListView;
    private TextView mEmptyView;

    private SimpleCursorAdapter mAdapter;

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
        mListView.setOnItemClickListener(this);

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.timeline_item, null,
                new String[] {StatusContract.Columns.USER, StatusContract.Columns.MESSAGE, StatusContract.Columns.CREATED_AT},
                new int[] {R.id.text_user, R.id.text_message, R.id.text_created_at},
                0);
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int createdAtIndex = cursor.getColumnIndex(StatusContract.Columns.CREATED_AT);
                if (createdAtIndex == columnIndex) {
                    long createdAt = cursor.getLong(createdAtIndex);
                    CharSequence dateString = YambaUtil.getCreatedAtString(createdAt);

                    TextView createdAtText = (TextView) view.findViewById(R.id.text_created_at);
                    createdAtText.setText(dateString);

                    return true;
                }

                return false;
            }
        });

        mListView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Uri uri = ContentUris.withAppendedId(StatusContract.CONTENT_URI, id);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI, null, null, null,
                StatusContract.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
