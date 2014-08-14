package com.hp.android.yamba;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TimelineActivity extends Activity implements TimelineFragment.OnTimelineInteractionListener {

    private boolean mInTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        View pane = findViewById(R.id.fragment_details);
        mInTwoPaneMode = (pane != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                intent = new Intent(this, RefreshService.class);
                startService(intent);
                return true;
            case R.id.action_post:
                intent = new Intent(this, StatusActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** TimelineFragment Callback */

    @Override
    public void onTimelineItemClick(Uri uri) {
        if (mInTwoPaneMode) {
            //Launch a fragment
            StatusDetailsFragment fragment = StatusDetailsFragment.newInstance(uri);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StatusDetailsActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}
