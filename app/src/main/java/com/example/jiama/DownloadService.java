package com.example.jiama;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.app.DownloadManager;

public class DownloadService extends Service {
    private String URLTODOWN="";
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        URLTODOWN=intent.getStringExtra("downloadurl");
        Log.d("服务启动",URLTODOWN);
        /************downloadmanager***********************/
        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(URLTODOWN);
        DownloadManager.Request request = new DownloadManager.Request(uri);
// 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("download", "536qipai.apk");
        request.setDescription("536qipai");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
// 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
// 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
// 把当前下载的ID保存起来
        SharedPreferences sPreferences = getSharedPreferences("downloadcomplete", 0);
        sPreferences.edit().putLong("refernece", refernece).commit();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("服务结束","服务结束了");

    }
}
