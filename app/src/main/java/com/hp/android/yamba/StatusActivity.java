package com.hp.android.yamba;

import android.app.Activity;
import android.content.Intent;
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
        final Intent callingIntent = getIntent();
        final String initialStatus = callingIntent.getStringExtra(StatusUpdateService.EXTRA_STATUS);
        mStatusText.setText(initialStatus);
    }

    public void onPostClick(View v) {
        final String status = mStatusText.getText().toString();
        Log.d(TAG, "Posting Status...");

        Intent intent = new Intent(this, StatusUpdateService.class);
        intent.putExtra(StatusUpdateService.EXTRA_STATUS, status);
        startService(intent);

        mStatusText.getText().clear();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
