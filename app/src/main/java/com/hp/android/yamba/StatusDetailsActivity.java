package com.hp.android.yamba;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


public class StatusDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_details);

        final Intent callingIntent = getIntent();
        Uri data = callingIntent.getData();

        StatusDetailsFragment fragment =
                (StatusDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        fragment.setStatus(data);
    }
}
