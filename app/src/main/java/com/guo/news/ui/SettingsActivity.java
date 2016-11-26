package com.guo.news.ui;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guo.news.R;
import com.guo.news.data.remote.NewsSyncAdapter;


public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.toolbar, root, false);
        Toolbar toolbar = (Toolbar) header.findViewById(R.id.toolbar);
        root.addView(header,0);

        setSupportActionBar(toolbar);
        setupActionBar();

        addPreferencesFromResource(R.xml.pref_general);

         ListPreference syncPreference = (ListPreference) findPreference(getString(R.string.pref_sync_frequency_key));
        syncPreference.setSummary(getPreferenceManager().getSharedPreferences()
                .getString(getString(R.string.pref_sync_frequency_key),getString(R.string.pref_sync_frequency_default)));
        syncPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int interval = Integer.parseInt((String) newValue);
                NewsSyncAdapter.changeSyncInterval(getApplicationContext(),interval);
                preference.setSummary((String) newValue);
                return true;
            }
        });

    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return false;
    }

}
