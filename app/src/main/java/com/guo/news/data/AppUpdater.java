package com.guo.news.data;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2016/11/27.
 */
public class AppUpdater {
    private static AppUpdater INSTANCE = null;
    private final DownloadManager downloadManager;
    private long id;
    private boolean downloading;
    private static final String APP_NAME = "News.apk";
    private final Uri destinationUri;
    private final Context mContext;

    public AppUpdater(Context context) {
        mContext = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        File destPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APP_NAME);
        destinationUri = Uri.fromFile(destPath);
    }

    public static AppUpdater getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppUpdater(context);
        }
        return INSTANCE;
    }

    public void update() {
        if (!downloading) {

            Uri fileUri = Uri.parse("https://get20000.herokuapp.com/apk");
            DownloadManager.Request request = new DownloadManager.Request(fileUri);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationUri(destinationUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Downloading news apk ...");
            id = downloadManager.enqueue(request);
            downloading = true;
        } else {
            Toast.makeText(mContext, "Current downloading", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel() {
        if (downloading) {
            downloadManager.remove(id);
        }
    }

    public void setFinish() {
        downloading = false;
    }

    public Uri getDownloadPath() {
        return destinationUri;
    }

    /**
     * Created by Administrator on 2016/11/27.
     */
    public static class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                    AppUpdater appUpdater = AppUpdater.getInstance(context);
                    appUpdater.setFinish();
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(appUpdater.getDownloadPath(), "application/vnd.android.package-archive");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                    break;
                case DownloadManager.ACTION_NOTIFICATION_CLICKED:

                    break;
            }
        }
    }
}
