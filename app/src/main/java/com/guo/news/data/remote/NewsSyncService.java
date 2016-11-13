package com.guo.news.data.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NewsSyncService extends Service {
    private static NewsSyncAdapter mNewsSyncAdapter = null;
    private static Object mSyncLock = new Object();
    public NewsSyncService() {
        synchronized (mSyncLock) {
            if (mNewsSyncAdapter == null) {
                mNewsSyncAdapter = new NewsSyncAdapter(this, true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mNewsSyncAdapter.getSyncAdapterBinder();
    }
}
