package com.guo.news.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String KEY_CONTENT_ID = "contentId";
    private static final int NEWS_LOADER = 100;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.news_image)
    ImageView news_image;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.byline)
    TextView byline;
    @Bind(R.id.body)
    TextView body;
    @Bind(R.id.add_comment)
    FloatingActionButton add_comment;

    private String mContentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        mContentId = getIntent().getStringExtra(KEY_CONTENT_ID);
        setSupportActionBar(toolbar);
        if (mContentId != null) {

            getSupportLoaderManager().initLoader(NEWS_LOADER, null, this);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContentEntity.buildContentWithIdUri(mContentId),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            title.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
            date.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE)));
            byline.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BYLINE)));
            body.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BODY)));
            Picasso.with(this)
                    .load(data.getString(data.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                    .into(news_image);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @OnClick(R.id.add_comment)
    public void click() {
        //TODO
    }
}
