package com.guo.news.appwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.news.PreferenceConstant;
import com.guo.news.data.local.NewsContract;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;

/**
 * Created by Administrator on 2016/11/21.
 */
public class SectionAdapter extends RecyclerViewCursorAdapter {

    private final SharedPreferences sharedPreferences;

    public SectionAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        sharedPreferences = mContext.getSharedPreferences(PreferenceConstant.MAIN_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        final String sectionId = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID));
        final String sectionTitle = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_WEB_TITLE));
        ((TextView) holder.itemView).setText(sectionTitle);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString(PreferenceConstant.KEY_APP_WIDGET_SECTION, sectionId);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,parent,false));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
        }
    }

}
