package com.guo.news.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.SectionEntity;
import com.guo.news.ui.widget.RecyclerViewCursorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/23.
 */
public class SectionAdapterV2 extends RecyclerViewCursorAdapter<SectionAdapterV2.ViewHolder> {

    public SectionAdapterV2(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_section, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.section.setText(cursor.getString(cursor.getColumnIndex(SectionEntity.COLUMN_WEB_TITLE)));
        holder.interested.setChecked(cursor.getInt(cursor.getColumnIndex(SectionEntity.COLUMN_INSTERTED)) == 1);
        final String sectionId = cursor.getString(cursor.getColumnIndex(SectionEntity.COLUMN_ID));

        holder.interested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SectionEntity.COLUMN_INSTERTED,isChecked ? 1 : 0);
                String where = SectionEntity.COLUMN_ID + "= ? ";
                String[] whereArgs = {sectionId};
                mContext.getContentResolver().update(SectionEntity.CONTENT_URI,
                        contentValues,
                        where,
                        whereArgs);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.section)
        TextView section;
        @Bind(R.id.interested)
        CheckBox interested;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
