package com.guo.news.appwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.news.PreferenceConstant;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;

/**
 * Created by Administrator on 2016/11/21.
 */
public class ListSectionAdapter extends RecyclerViewCursorAdapter {

    private final SharedPreferences sharedPreferences;

    public ListSectionAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        final String sectionId = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID));
        final String sectionTitle = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_WEB_TITLE));
//        holder.itemView.setClickable(true);
        ((TextView) holder.itemView).setText(sectionTitle);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString(PreferenceConstant.KEY_APP_WIDGET_SECTION, sectionId).apply();
                Intent intent = new Intent(mContext,NewsListWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(new ComponentName(mContext, NewsListWidgetProvider.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                mContext.sendBroadcast(intent);
                ((Activity) mContext).finish();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_text,parent,false));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
