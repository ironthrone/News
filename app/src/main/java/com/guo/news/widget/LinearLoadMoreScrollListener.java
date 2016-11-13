package com.guo.news.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/11/13.
 */
public abstract class LinearLoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private static final int PREPARE_ITEM_COUNT = 3;
    private boolean mLoading;
    private int previousTotalItem;
    private int mCurrentPage;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int totalItemCount = linearLayoutManager.getItemCount();

        if (mLoading) {
            if (totalItemCount > previousTotalItem) {
                mLoading = true;
                previousTotalItem = totalItemCount;
            }
        }

        if (!mLoading && lastVisibleItemPosition >= totalItemCount - PREPARE_ITEM_COUNT) {

            mCurrentPage++;
            loadMore(mCurrentPage);
            mLoading = true;
        }
    }

    public abstract void loadMore(int page) ;
}
