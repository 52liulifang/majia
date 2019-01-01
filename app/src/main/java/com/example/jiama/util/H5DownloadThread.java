package com.example.jiama.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class H5DownloadThread extends Thread {
    private String urlpath;

    public H5DownloadThread(String url) {

        this.urlpath = url;
    }
    @Override
    public void run() {
        try {
            URL url=new URL(urlpath);
            URLConnection conn=url.openConnection();
            InputStream is=conn.getInputStream();
            //检测sdcard是否挂载
            if(Environment.getExternalStorageState()==(Environment.MEDIA_MOUNTED)){
                File file=new File(Environment.getExternalStorageDirectory(),"yingyong.apk");
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream os = new FileOutputStream(file);
                byte[] array=new byte[1024];
                int index=is.read(array);
                while(index!=-1){
                    os.write(array, 0, index);
                    index=is.read(array);
                }
                if(os!=null){
                    os.flush();
                    os.close();
                }
                if(is!=null){
                    is.close();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
