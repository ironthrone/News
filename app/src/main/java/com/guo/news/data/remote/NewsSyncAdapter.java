package com.guo.news.data.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.guo.news.PreferenceConstant;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.model.ResponseModel;
import com.guo.news.data.model.SectionModel;
import com.guo.news.ui.MainActivity;
import com.guo.news.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = NewsSyncAdapter.class.getSimpleName();
    private int mUpdatedSectionCount;

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
                .map(new Func1<List<SectionModel>, List<String>>() {
                    @Override
                    public List<String> call(List<SectionModel> sectionModels) {
                        String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
                        String[] whereArgs = new String[]{"1"};
                        Cursor cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
                                null, where, whereArgs, null);
                        if (cursor != null && cursor.getCount() == 0) {
                            int sectionNum = sectionModels.size();
                            Random random = new Random();
                            for (int i = 0; i < 6; i++) {
                                sectionModels.get(random.nextInt(sectionNum)).insterested = true;
                            }
                        }

                        int insertedRows = Utility.insertSections(getContext(), sectionModels);
                        Log.d(TAG, "insert section for " + insertedRows + " rows");

                        // sync the interested section
                        cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
                                null, where, whereArgs, null);
                        List<String> randomLoadSections = new ArrayList<>();
                        if (cursor != null && cursor.moveToFirst()) {
                            try {

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                String widgetSectionId = sharedPreferences.getString(PreferenceConstant.KEY_APP_WIDGET_SECTION, PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
                                boolean setWidgetSection = widgetSectionId.equals(PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);


                                do {
                                    mUpdatedSectionCount++;
                                    String sectionId = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID));
                                    randomLoadSections.add(sectionId);
                                    if (setWidgetSection) {

                                        sharedPreferences.edit().putString(PreferenceConstant.KEY_APP_WIDGET_SECTION, sectionId).apply();
                                        setWidgetSection = false;
                                    }
                                } while (cursor.moveToNext());
                            } finally {
                                cursor.close();
                            }
                        }
                        return randomLoadSections;
                    }
                })
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .flatMap(new Func1<String, Observable<ResponseModel<List<ContentModel>>>>() {
                    @Override
                    public Observable<ResponseModel<List<ContentModel>>> call(String s) {
                        return ServiceHost.getService().getContentFromSection(s, null, 50, null, null);
                    }
                })
                .map(new ResultTransformer<List<ContentModel>>())
                .subscribe(new Action1<List<ContentModel>>() {
                    @Override
                    public void call(List<ContentModel> contentModels) {
                        Log.d(TAG, "pull content successful");
                        Utility.insertContents(getContext(), contentModels);
                        mUpdatedSectionCount--;
                        if (mUpdatedSectionCount == 0) {
                            notifyUser();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mUpdatedSectionCount--;
                        throwable.printStackTrace();
                    }
                });

//        ServiceHost.getService().getSectionList()
//                .map(new ResultTransformer<List<SectionModel>>())
//                .subscribe(new Action1<List<SectionModel>>() {
//                    @Override
//                    public void call(List<SectionModel> sectionModels) {
//                        String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
//                        String[] whereArgs = new String[]{"1"};
//                        Cursor cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
//                                null, where, whereArgs, null);
//                        if (cursor != null && cursor.getCount() == 0) {
//                            int sectionNum = sectionModels.size();
//                            Random random = new Random();
//                            for (int i = 0; i < 6; i++) {
//                                sectionModels.get(random.nextInt(sectionNum)).insterested = true;
//                            }
//                        }
//
//                        int insertedRows = Utility.insertSections(getContext(), sectionModels);
//                        Log.d(TAG, "insert section for " + insertedRows + " rows");
//
//                        // sync the interested section
//                        cursor = getContext().getContentResolver().query(NewsContract.SectionEntity.CONTENT_URI,
//                                null, where, whereArgs, null);
//
//                        if (cursor != null && cursor.moveToFirst()) {
//                            try {
//
//                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//                                String widgetSectionId = sharedPreferences.getString(PreferenceConstant.KEY_APP_WIDGET_SECTION, PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
//                                boolean setWidgetSection = widgetSectionId.equals(PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
//
//
//                                do {
//                                    String sectionId = cursor.getString(cursor.getColumnIndex(NewsContract.SectionEntity.COLUMN_ID));
//
//                                    if (setWidgetSection) {
//
//                                        sharedPreferences.edit().putString(PreferenceConstant.KEY_APP_WIDGET_SECTION, sectionId).apply();
//                                        setWidgetSection = false;
//                                    }
//
//                                    Log.d(TAG, "pull content from section: " + sectionId);
//                                    ServiceHost.getService().getContentFromSection(sectionId, null, 50, null, null)
//                                            .map(new ResultTransformer<List<ContentModel>>())
//                                            .subscribe(new Action1<List<ContentModel>>() {
//                                                @Override
//                                                public void call(List<ContentModel> contentModels) {
//                                                    Log.d(TAG, "pull content successful");
//                                                    Utility.insertContents(getContext(), contentModels);
//                                                }
//                                            }, new Action1<Throwable>() {
//                                                @Override
//                                                public void call(Throwable throwable) {
//                                                    throwable.printStackTrace();
//                                                }
//                                            });
//                                } while (cursor.moveToNext());
//                            } finally {
//                                cursor.close();
//                            }
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                });
    }

    private void notifyUser() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("News")
                .setContentText("Hello, your news has updated!");

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.setContentIntent(PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
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

    public static void changeSyncInterval(Context context, int interval) {

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
        ContentResolver.addPeriodicSync(account,
                AUTHORITY, new Bundle(), syncInterval);

        //set account is syncable
        ContentResolver.setIsSyncable(account, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, AUTHORITY, bundle);
    }

}
