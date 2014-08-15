package com.hp.android.yamba;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class StatusActivity extends Activity implements TextWatcher,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = StatusActivity.class.getSimpleName();

    private static final int MAX_CHARS = 140;

    private TextView mStatusCounter;
    private EditText mStatusText;
    private Button mStatusButton;

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    private Location mLocation;

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

        mLocationClient = new LocationClient(this, this, this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationClient.isConnected()) {
            //Disable existing updates
            mLocationClient.removeLocationUpdates(this);
        }

        mLocationClient.disconnect();
    }

    public void onPostClick(View v) {
        final String status = mStatusText.getText().toString();
        Log.d(TAG, "Posting Status...");

        Intent intent = new Intent(this, StatusUpdateService.class);
        intent.putExtra(StatusUpdateService.EXTRA_STATUS, status);
        if (mLocation != null) {
            intent.putExtra(StatusUpdateService.EXTRA_LOCATION, mLocation);
        }
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

    /** Google Play Services Callbacks */

    @Override
    public void onConnected(Bundle bundle) {
        mLocation = mLocationClient.getLastLocation();
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        mLocation = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /** LocationListener Callbacks */

    @Override
    public void onLocationChanged(Location location) {
        LogUtil.d(this, "Location Received: "+location.getLatitude()+", "+location.getLongitude());
        mLocation = location;
    }
}
