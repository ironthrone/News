package com.guo.news.data.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.model.SectionModel;
import com.guo.news.util.Utility;

import java.util.List;
import java.util.Random;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = NewsSyncAdapter.class.getSimpleName();
    // 4 hours

    public NewsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public NewsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }


    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        Log.d(TAG, "perform sync");

        ServiceHost.getService().getSectionList()
                .map(new ResultTransformer<List<SectionModel>>())
                .subscribe(new Action1<List<SectionModel>>() {
                    @Override
                    public void call(List<SectionModel> sectionModels) {
                        String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
                        String[] whereArgs = new String[]{"1"};
                        Cursor cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
                                null, where, whereArgs, null);
                        if (cursor != null && cursor.getCount() == 0) {
                            int sectionNum = sectionModels.size();
                            Random random = new Random();
                            for(int i = 0;i < 6;i++) {
                                sectionModels.get(random.nextInt(sectionNum)).insterested = true;
                            }
                        }

                        int insertedRows = Utility.insertSections(getContext(), sectionModels);
                        Log.d(TAG, "insert section for " + insertedRows + " rows");

                        // sync the interested section
                        cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
                                null, where, whereArgs, null);
                        if (cursor != null && cursor.moveToFirst()){
                            try {

                                do {
                                    String sectionId = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID));
                                    Log.d(TAG, "pull content from section: " + sectionId);
                                    ServiceHost.getService().getContentFromSection(sectionId, null, 50, null, null)
                                            .map(new ResultTransformer<List<ContentModel>>())
                                            .subscribe(new Action1<List<ContentModel>>() {
                                                @Override
                                                public void call(List<ContentModel> contentModels) {
                                                    Log.d(TAG, "pull content successful");
                                                    Utility.insertContents(getContext(), contentModels);
                                                }
                                            }, new Action1<Throwable>() {
                                                @Override
                                                public void call(Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }
                                            });
                                } while (cursor.moveToNext());
                            } finally {
                                cursor.close();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    public static void initializeSync(Context context) {
        Account account = new Account(context.getString(R.string.account_name), context.getString(R.string.account_type));
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        //if already add account ,ignore these code
        if (accountManager.getPassword(account) == null) {
            if (accountManager.addAccountExplicitly(account, "", null)) {
                onCreateAccount(account, context);
            }
        }
    }

    public static void changeSyncInterval(Context context,int interval) {

        String AUTHORITY = context.getString(R.string.news_provider_authority);
        Account account = new Account(context.getString(R.string.account_name), context.getString(R.string.account_type));

        //change minite to second
        interval = interval * 60;
        ContentResolver.addPeriodicSync(account,
                AUTHORITY, new Bundle(), interval);
    }

    private static void onCreateAccount(Account account, Context context) {
        String AUTHORITY = context.getString(R.string.news_provider_authority);
        String sync = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_sync_frequency_key),
                context.getString(R.string.pref_sync_frequency_default));
        int syncInterval = Integer.parseInt(sync) * 60;


        //set account is syncable
        ContentResolver.setIsSyncable(account, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, AUTHORITY, bundle);
    }

}
