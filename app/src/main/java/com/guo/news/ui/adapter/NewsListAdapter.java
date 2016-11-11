package com.guo.news.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/11.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Cursor mCursor;
    private  Context mContext;

    public NewsListAdapter(Context context,Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            Picasso.with(mContext)
                    .load(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                    .into(holder.image);
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
            holder.date.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE)));
            holder.stand_first.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_STANDFIRST)));
        }
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.stand_first)
        TextView stand_first;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
