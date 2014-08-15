package com.hp.android.yamba;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;


public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        setRefreshSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final String refreshKey = getString(R.string.pref_key_refresh);
        if (TextUtils.equals(refreshKey, key)) {
            setRefreshSummary();
        }
    }

    private void setRefreshSummary() {
        final String refreshKey = getString(R.string.pref_key_refresh);
        ListPreference preference = (ListPreference) findPreference(refreshKey);

        if (TextUtils.equals("0", preference.getValue())) {
            preference.setSummary("Auto-Refresh Disabled");
        } else {
            CharSequence selected = preference.getEntry();
            preference.setSummary("Refresh interval set to " + selected);
        }
    }
}
