package com.guo.news.ui;

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

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.remote.NewsSyncAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SECTION_LOADER = 100;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tab_layout;
    @Bind(R.id.view_pager)
    ViewPager view_pager;
    private NewsFragmentPagerAdapter mNewsFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        getSupportLoaderManager().initLoader(SECTION_LOADER, null, this);
        NewsSyncAdapter.initlizeSync(getApplicationContext());
    }



    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NewsContract.SectionEntity.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mNewsFragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), data);
        view_pager.setAdapter(mNewsFragmentPagerAdapter);
        tab_layout.setupWithViewPager(view_pager);

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

        public void removeCursor() {
            mSectionCursor = null;
        }

        @Override
        public Fragment getItem(int position) {
            if (mSectionCursor.moveToPosition(position)) {

                return NewsFragment.getInstance(mSectionCursor.getString(mSectionCursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID)));
            }
            return null;
        }

        @Override
        public int getCount() {
            return mSectionCursor.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mSectionCursor.moveToPosition(position)) {
                return mSectionCursor.getString(mSectionCursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_WEB_TITLE));
            }
            return super.getPageTitle(position);
        }
    }
}
