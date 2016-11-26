package com.guo.news.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.ui.adapter.ChipsSectionAdapter;
import com.guo.news.util.MeasureConverter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectInterestedSectionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SECTION_LOADER = 100;
    @Bind(R.id.interested_sections)
    RecyclerView interested_sections;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ChipsSectionAdapter mSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interested_section);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
//                .setScrollingEnabled(true)
                .setMaxViewsInRow(6)
                .build();


        interested_sections.setLayoutManager(chipsLayoutManager);

        mSectionAdapter = new ChipsSectionAdapter(this, null);
        interested_sections.setAdapter(mSectionAdapter);
        interested_sections.addItemDecoration(new SpacingItemDecoration(MeasureConverter.dip2px(this,4),MeasureConverter.dip2px(this,4)));

        getSupportLoaderManager().initLoader(SECTION_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mSectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSectionAdapter.setCursor(null);
    }
}
