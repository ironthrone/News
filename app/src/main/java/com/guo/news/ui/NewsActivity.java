package com.guo.news.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.CommentModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;
import com.guo.news.ui.adapter.CommentAdapter;
import com.guo.news.ui.widget.AbstractTransitionListener;
import com.guo.news.ui.widget.AsyncImageGetter;
import com.guo.news.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String KEY_CONTENT_ID = "contentId";
    private static final int NEWS_LOADER = 100;
    private static final int COMMENTS_LOADER = 200;
    private static final String TAG = NewsActivity.class.getSimpleName();
    @Bind(R.id.app_bar)
    AppBarLayout app_bar;
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
    @Bind(R.id.comment_list)
    RecyclerView comment_list;

    private String mContentId;
    private CommentAdapter mCommentAdapter;
    private ShareActionProvider mShareActionProvider;
    private String mShareUrl;
    private boolean mRevealed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        mContentId = getIntent().getStringExtra(KEY_CONTENT_ID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        comment_list.setLayoutManager(new LinearLayoutManager(this));
        mCommentAdapter = new CommentAdapter(this, null);
        comment_list.setAdapter(mCommentAdapter);

        mShareActionProvider = new ShareActionProvider(this) {
            @Override
            public View onCreateActionView() {
                return null;
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        v.removeOnLayoutChangeListener(this);
                        int centerX = app_bar.getWidth() / 2;
                        int centerY = app_bar.getHeight() / 2;
                        double endRadius = Math.hypot(centerX, centerY);
                        ViewAnimationUtils.createCircularReveal(app_bar, centerX, centerY, 0, ((float) endRadius)).start();

                    }
                });
        }


        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
//        MenuItem shareItem = menu.findItem(R.id.share);
//        shareItem.setActionProvider(mShareActionProvider);
//        MenuItemCompat.setActionProvider(shareItem, mShareActionProvider);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        return super.onCreateOptionsMenu(menu);
    }


    private void loadData() {
        if (mContentId != null) {
            getSupportLoaderManager().initLoader(NEWS_LOADER, null, this);
            getSupportLoaderManager().initLoader(COMMENTS_LOADER, null, this);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            case R.id.share:
                if (mShareUrl != null) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, mShareUrl);
                    startActivity(Intent.createChooser(share, getString(R.string.share_chooser_title)));
                }
                return true;
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case NEWS_LOADER:
                return new CursorLoader(this, ContentEntity.buildContentWithIdUri(mContentId),
                        null,
                        null,
                        null,
                        null);
            case COMMENTS_LOADER:
                return new CursorLoader(this,
                        NewsContract.CommentEntity.buildWithContentIDUrl(mContentId),
                        null,
                        null,
                        null,
                        null);
            default:
                throw new UnsupportedOperationException();
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case NEWS_LOADER:
                if (data.moveToFirst()) {
                    mShareUrl = data.getString(data.getColumnIndex(ContentEntity.COLUMN_WEB_URL));

                    title.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
                    date.setText(Utility.removeCharInDate(data.getString(data.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE))));
                    byline.setText(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BYLINE)));

                    Html.ImageGetter imageGetter = new AsyncImageGetter(getApplicationContext(), body);

                    body.setText(Html.fromHtml(data.getString(data.getColumnIndex(ContentEntity.COLUMN_BODY)), imageGetter, null));
                    body.setMovementMethod(LinkMovementMethod.getInstance());
                    Picasso.with(this)
                            .load(data.getString(data.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                            .into(news_image);

//                    Palette palette = Palette.from(((BitmapDrawable) news_image.getDrawable()).getBitmap())
//                            .generate();
//                    Palette.Swatch vibrant = palette.getVibrantSwatch();
//                    if (vibrant != null) {
//                        toolbar.setBackgroundColor(vibrant.getRgb());
//                        toolbar.setTitleTextColor(vibrant.getTitleTextColor());
//                    }


                }
                break;
            case COMMENTS_LOADER:
                if (data == null || data.getCount() == 0) {
                    ServiceHost.getService().getCommentList(mContentId, 1)
                            .subscribeOn(Schedulers.io())
                            .map(new ResultTransformer<List<CommentModel>>())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<CommentModel>>() {
                                @Override
                                public void call(List<CommentModel> commentModels) {
                                    if (commentModels == null) {
                                        Log.d(TAG, "get comment fail");
                                    }
                                    Utility.insertComments(getApplicationContext(), commentModels);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            });
                } else {
                    mCommentAdapter.setCursor(data);
                    mCommentAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCommentAdapter.setCursor(null);
    }

    @OnClick(R.id.add_comment)
    public void click() {
        Intent intent = new Intent(NewsActivity.this, CommentActivity.class);
        intent.putExtra(CommentActivity.KEY_CONTENT_ID, mContentId);
        startActivity(intent);
        overridePendingTransition(R.anim.bottom_to_up, android.R.anim.fade_out);
    }
}
