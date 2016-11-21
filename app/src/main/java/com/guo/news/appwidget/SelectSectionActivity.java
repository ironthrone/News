package com.guo.news.appwidget;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;

public class SelectSectionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SECTION_LOADER = 100;
    private SectionAdapter mSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_section_id);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        RecyclerView section_list  = (RecyclerView) findViewById(R.id.section_list);
        mSectionAdapter = new SectionAdapter(this,null);
        section_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        section_list.setAdapter(mSectionAdapter);
        getSupportLoaderManager().initLoader(SECTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NewsContract.SectionEntity.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSectionAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSectionAdapter.setCursor(null);
    }
}
