package com.guo.news.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.guo.news.NewsApplication;
import com.guo.news.R;
import com.guo.news.data.AppUpdater;
import com.guo.news.data.remote.NewsSyncAdapter;


public class SettingsActivity extends AppCompatPreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.toolbar, root, false);
        Toolbar toolbar = (Toolbar) header.findViewById(R.id.toolbar);
        root.addView(header, 0);

        setSupportActionBar(toolbar);
        setupActionBar();

        addPreferencesFromResource(R.xml.pref_general);

        ListPreference syncPreference = (ListPreference) findPreference(getString(R.string.pref_sync_frequency_key));
        syncPreference.setSummary(getPreferenceManager().getSharedPreferences()
                .getString(getString(R.string.pref_sync_frequency_key), getString(R.string.pref_sync_frequency_default)));
        Preference downloadPreference =  findPreference(getString(R.string.pref_download_key));
        downloadPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Confirm to update")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppUpdater.getInstance(getApplicationContext()).update();
                                dialog.dismiss();
                                Tracker tracker = ((NewsApplication) getApplication()).getTracker();
                                tracker.send(new HitBuilders.EventBuilder()
                                        .setCategory("Setting")
                                        .setAction("Download")
                                        .setLabel("Download")
                                        .build());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return false;
            }
        });

        syncPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int interval = Integer.parseInt((String) newValue);
                NewsSyncAdapter.changeSyncInterval(getApplicationContext(), interval);
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
