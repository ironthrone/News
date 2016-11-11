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

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.ui.adapter.NewsListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<Cursor>{


    private static final String KEY_SECTION_ID = "section_id";
    private static final String TAG = NewsFragment.class.getSimpleName();
    private static final int LOADER_CONTENT = 100;
    private String mSectionId;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    private NewsListAdapter mAdapter;

    public static NewsFragment getInstance(String sectionId) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SECTION_ID,sectionId);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this,getView());
        swipe_refresh.setOnRefreshListener(this);

        mAdapter = new NewsListAdapter(getContext(), null);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_CONTENT, null, this);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = NewsContract.ContentEntity.COLUMN_WEB_PUBLICATION_DATE + " desc";
        return new CursorLoader(getContext(),
                NewsContract.ContentEntity.buildContentWithSectionUri(mSectionId),
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            //TODO getnewerdate
        }
        mAdapter.setCursor(data);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.setCursor(null);
    }
}
