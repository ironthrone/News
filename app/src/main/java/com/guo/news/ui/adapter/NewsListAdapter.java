package com.guo.news.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;
import com.guo.news.util.Utility;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/11.
 */
public class NewsListAdapter extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {


    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_NO_IMAGE = 2;
    private final OnItemClickListener mOnItemClickListener;

    public NewsListAdapter(Context context, Cursor cursor, OnItemClickListener onItemClickListener) {
        super(context, cursor);
        mOnItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false));
        } else {
            return new NoImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_news_no_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder instanceof ViewHolder) {
            final ViewHolder finalHolder = (ViewHolder) holder;
            final String contentId = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_ID));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(finalHolder, contentId);
                    }

                }
            });
            Picasso.with(mContext)
                    .load(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL)))
                    .into(finalHolder.image);
            finalHolder.title.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
            finalHolder.date.setText(Utility.removeCharInDate(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE))));
            finalHolder.stand_first.setText(Html.fromHtml(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_TRAIL_TEXT))));

        } else {
            final NoImageViewHolder finalHolder = (NoImageViewHolder) holder;
            final String contentId = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_ID));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(finalHolder, contentId);
                    }

                }
            });
            finalHolder.title.setText(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE)));
            finalHolder.date.setText(Utility.removeCharInDate(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_WEB_PUBLICATION_DATE))));
            finalHolder.stand_first.setText(Html.fromHtml(mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_TRAIL_TEXT))));
        }

    }


    @Override
    public int getItemViewType(int position) {
        mCursor.moveToPosition(position);
        String thumbnail = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL));
        if (thumbnail == null) {
            return TYPE_NO_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, String contentId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        public ImageView image;
        @Bind(R.id.title)
        public TextView title;
        @Bind(R.id.date)
        public TextView date;
        @Bind(R.id.stand_first)
        public TextView stand_first;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class NoImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        public TextView title;
        @Bind(R.id.date)
        public TextView date;
        @Bind(R.id.stand_first)
        public TextView stand_first;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
