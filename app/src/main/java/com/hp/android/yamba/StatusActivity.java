package com.hp.android.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends Activity implements TextWatcher {

    private static final String TAG = StatusActivity.class.getSimpleName();

    private static final int MAX_CHARS = 140;

    private TextView mStatusCounter;
    private EditText mStatusText;
    private Button mStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mStatusCounter = (TextView) findViewById(R.id.status_counter);
        mStatusText = (EditText) findViewById(R.id.status_text);
        mStatusButton = (Button) findViewById(R.id.status_button);

        mStatusText.addTextChangedListener(this);
        if (savedInstanceState == null) {
            mStatusText.setText(null);
        }
    }

    public void onPostClick(View v) {
        final String status = mStatusText.getText().toString();
        Log.d(TAG, "Posting Status...");

        StatusUpdateTask task = new StatusUpdateTask();
        task.execute(status);
    }

    private class StatusUpdateTask extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(StatusActivity.this,
                    "Posting...", "Posting Status", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final String status = params[0];
            YambaClient client = new YambaClient("student", "password");

            try {
                client.postStatus(status);
                Log.d(TAG, "Status Complete");
            } catch (YambaClientException e) {
                Log.wtf(TAG, "Error Posting Status", e);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgress.dismiss();
            if (result) {
                mStatusText.getText().clear();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        int remaining = MAX_CHARS - s.length();
        mStatusCounter.setText(String.valueOf(remaining));

        mStatusButton.setEnabled(remaining >= 0);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
