package com.guo.news.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.remote.NewsSyncAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SECTION_LOADER = 100;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tab_layout;
    @Bind(R.id.view_pager)
    ViewPager view_pager;
    @Bind(R.id.banner_ad)
    AdView banner_ad;
    private NewsFragmentPagerAdapter mNewsFragmentPagerAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mNewsFragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), null);
        view_pager.setAdapter(mNewsFragmentPagerAdapter);
        tab_layout.setupWithViewPager(view_pager);

        getSupportLoaderManager().initLoader(SECTION_LOADER, null, this);

        NewsSyncAdapter.initializeSync(getApplicationContext());

        AdRequest adRequest = new AdRequest.Builder().build();
        banner_ad.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putString("Screen","Main");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.select:
                Intent intent = new Intent(this, SelectInterestedSectionActivity.class);
                startActivity(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
        String[] whereArgs = new String[]{"1"};
        return new CursorLoader(this,
                NewsContract.SectionEntity.CONTENT_URI,
                null,
                where,
                whereArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            Log.d(TAG, "load finish, but cursor is null");
        } else {
            Log.d(TAG, "load finish cursor count is: " + data.getCount());
        }
        mNewsFragmentPagerAdapter.setCursor(data);
        mNewsFragmentPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mNewsFragmentPagerAdapter.removeCursor();
    }


    public static class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private Cursor mSectionCursor;

        public NewsFragmentPagerAdapter(FragmentManager fm, Cursor sectionCursor) {
            super(fm);
            mSectionCursor = sectionCursor;
        }

        public void setCursor(Cursor cursor) {
            mSectionCursor = cursor;
        }

        public void removeCursor() {
            mSectionCursor = null;
        }

        @Override
        public Fragment getItem(int position) {
//            if (mSectionCursor.moveToPosition(position)) {

            mSectionCursor.moveToPosition(position);
            return NewsListFragment.getInstance(mSectionCursor.getString(mSectionCursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID)));
//            }
//            return null;
        }

        @Override
        public int getCount() {
            if (mSectionCursor == null) return 0;
            return mSectionCursor.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mSectionCursor.moveToPosition(position)) {
                return mSectionCursor.getString(mSectionCursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_WEB_TITLE));
            }
            return super.getPageTitle(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
