package com.guo.news.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;
import com.guo.news.ui.adapter.NewsListAdapter;
import com.guo.news.ui.widget.DividerItemDecoration;
import com.guo.news.ui.widget.LinearLoadMoreScrollListener;
import com.guo.news.util.DateUtils;
import com.guo.news.util.MeasureConverter;
import com.guo.news.util.Utility;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String KEY_SECTION_ID = "section_id";
    private static final String TAG = NewsFragment.class.getSimpleName();
    private static final int LOADER_CONTENT = 100;
    private static final String DATE_TEMPLE = "yyyy-MM-dd";
    private static final int DIVIDER_HEIGHT = 4;
    private String mSectionId;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    private NewsListAdapter mAdapter;
    private boolean mFirstLoaded = true;
    /**
     * last item's date in cp
     */
    private String mLastDateInCP;


    private int mRefreshPage = 1;
    private LinearLoadMoreScrollListener mLoadMoreListener;

    public static NewsFragment getInstance(String sectionId) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SECTION_ID, sectionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSectionId = getArguments().getString(KEY_SECTION_ID);
        if (mSectionId == null) {
            Log.d(TAG, "section id is null ");
        }
        mLastDateInCP = DateUtils.format(System.currentTimeMillis(), DATE_TEMPLE);

        mLoadMoreListener = new LinearLoadMoreScrollListener() {
            @Override
            public void loadMore(int page) {
                ServiceHost.getService().getContentFromSection(mSectionId, page, 10, null, mLastDateInCP)
                        .map(new ResultTransformer<List<ContentModel>>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<ContentModel>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(List<ContentModel> contentModels) {
                                if (contentModels.size() == 0) {
                                    Toast.makeText(getContext(), "there is'nt  value", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Utility.insertContents(getContext(),contentModels) < 0) {
                                    Log.d(TAG, "load more insert fail");
                                }
                            }
                        });

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        swipe_refresh.setOnRefreshListener(this);

        mAdapter = new NewsListAdapter(getContext(), null);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(mAdapter);
        recycler_view.addItemDecoration(new DividerItemDecoration(MeasureConverter.dip2px(getContext(),DIVIDER_HEIGHT)));
        recycler_view.addOnScrollListener(mLoadMoreListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_CONTENT, null, this);
    }


    @Override
    public void onRefresh() {
        ServiceHost.getService().getContentFromSection(mSectionId, mRefreshPage++, 10, null, null)
                .subscribeOn(Schedulers.io())
                .map(new ResultTransformer<List<ContentModel>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ContentModel>>() {
                    @Override
                    public void call(List<ContentModel> contentModels) {
                        Utility.insertContents(NewsFragment.this.getContext(), contentModels);
                        if (contentModels.size() < 0) {

                            Toast.makeText(NewsFragment.this.getContext(), "There is no newer data!", Toast.LENGTH_SHORT).show();
                        }
                        swipe_refresh.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        swipe_refresh.setRefreshing(false);

                    }
                });
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = ContentEntity.COLUMN_WEB_PUBLICATION_DATE + " desc";
        return new CursorLoader(getContext(),
                ContentEntity.buildContentWithSectionUri(mSectionId),
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            Log.d(TAG, "load finish,but cursor is null");

        } else {
            Log.d(TAG, "load finish cursor count is: " + data.getCount());
        }
        if (mFirstLoaded) {
            if (data != null && data.moveToLast()) {
                mLastDateInCP = data.getString(data.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE));
                mLastDateInCP = mLastDateInCP.substring(0, DATE_TEMPLE.length());
                mFirstLoaded = false;
            }
        }
        mAdapter.setCursor(data);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.setCursor(null);
    }
}
