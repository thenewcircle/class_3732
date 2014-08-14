package com.hp.android.yamba;


import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatusDetailsFragment extends Fragment {

    public static StatusDetailsFragment newInstance(Uri uri) {
        StatusDetailsFragment f = new StatusDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable("uri", uri);

        f.setArguments(args);

        return f;
    }

    public StatusDetailsFragment() {
        // Required empty public constructor
    }

    private TextView mUserText;
    private TextView mMessageText;
    private TextView mCreatedAtText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_status_details, container, false);

        mUserText = (TextView) root.findViewById(R.id.text_user);
        mMessageText = (TextView) root.findViewById(R.id.text_message);
        mCreatedAtText = (TextView) root.findViewById(R.id.text_created_at);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            Uri uri = args.getParcelable("uri");
            setStatus(uri);
        }
    }

    public void setStatus(Uri uri) {
        Cursor item = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (item == null || !item.moveToFirst()) {
            return;
        }

        String user = item.getString(item.getColumnIndex(StatusContract.Columns.USER));
        String message = item.getString(item.getColumnIndex(StatusContract.Columns.MESSAGE));

        long createdAt = item.getLong(item.getColumnIndex(StatusContract.Columns.CREATED_AT));
        CharSequence dateString = YambaUtil.getCreatedAtString(createdAt);

        item.close();

        mUserText.setText(user);
        mMessageText.setText(message);
        mCreatedAtText.setText(dateString);
    }
}
