package com.guo.news.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.ui.NewsActivity;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/11.
 */
public class NewsListAdapter extends RecyclerViewCursorAdapter<NewsListAdapter.ViewHolder> {


    public NewsListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final String contentId = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_ID));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                intent.putExtra(NewsActivity.KEY_CONTENT_ID, contentId);
                mContext.startActivity(intent);
            }
        });
        Picasso.with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                .into(holder.image);
        holder.title.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
        holder.date.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE)));
        holder.stand_first.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_TRAIL_TEXT)));

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
            ButterKnife.bind(this, itemView);
        }
    }
}
