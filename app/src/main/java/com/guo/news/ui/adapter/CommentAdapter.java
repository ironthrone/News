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
import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/21.
 */
public class CommentAdapter extends RecyclerViewCursorAdapter<CommentAdapter.ViewHolder> {
    public CommentAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.comment_body.setText(cursor.getString(cursor.getColumnIndex(CommentEntity.COLUMN_CONTENT)));
        holder.comment_date.setText(cursor.getString(cursor.getColumnIndex(CommentEntity.COLUMN_ADD_TIME)));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment,parent,false));
    }


     static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avatar)
        ImageView avatar;
        @Bind(R.id.user_name)
        TextView user_name;
        @Bind(R.id.comment_body)
        TextView comment_body;
        @Bind(R.id.comment_date)
        TextView comment_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
