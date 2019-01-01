package com.example.jiama;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.jiama.util.HttpUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {
    private String url_base64;
    private int MODE;
    private int erro_code;
    private int rt_code;
    private int show_url;
    public static final String PACK_NAME = "com.qipai.qipai536";//微信包名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        /******************base64解码测试***********************/
        String str2 = new String(Base64.decode("eyJpZCI6IjEiLCIwIjoiMSIsInVybCI6InRlc3QuMzMzIiwiMSI6InRlc3QuMzMzIiwidHlwZSI6ImFuZHJvaWQiLCIyIjoiYW5kcm9pZCIsInNob3dfdXJsIjoiMSIsIjMiOiIxIiwiYXBwaWQiOiJ0ZXN0IiwiNCI6InRlc3QiLCJjb21tZW50IjoidGVzdCIsIjUiOiJ0ZXN0IiwiY3JlYXRlQXQiOiIyMDE3LTA5LTA1IDA0OjUzOjA2IiwiNiI6IjIwMTctMDktMDUgMDQ6NTM6MDYiLCJ1cGRhdGVBdCI6IjIwMTctMDktMDUgMDQ6NTM6MDYiLCI3IjoiMjAxNy0wOS0wNSAwNDo1MzowNiJ9", Base64.DEFAULT));
        Log.d("json",str2);
        /***************************************/
        if (isInstallApp(MainActivity.this)){
            Toast.makeText(this,"已经存在应用",Toast.LENGTH_SHORT).show();
            /**************自删除****************/
        Uri packageUri = Uri.parse("package:"+MainActivity.this.getPackageName());
        Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);
        startActivity(intent);}
        else{
            //Toast.makeText(this,"没有此应用",Toast.LENGTH_SHORT).show();
            requestData();
        }

    }
    private void requestData(){
        String requesturl="http://11.kaiguan118.com/back/get_init_data.php?type=android&appid=287";
       /***************判断尾字符*********************/
        if (requesturl.endsWith("id=287"))
            Log.d("TestofFind","测试成功");
        /********************************************/
        HttpUtil.sendOkHttpRequest(requesturl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strdata=response.body().string();
                //方法
                parseJSONWithJSONObject(strdata);

                Log.d("bacdata",strdata);
                //http://11.kaiguan118.com/back/get_init_data.php?type=android&appid=287
            }
        });
    }
    private  void parseJSONWithJSONObject(String jsonData){

        try{
            //Log.d("rt_code","iugiyfgyujfg");
                JSONObject jsonObject=new JSONObject(jsonData);
                url_base64=jsonObject.getString("data");
                rt_code=jsonObject.getInt("rt_code");
                if (rt_code==201){
                    erro_code=jsonObject.getInt("type");
                    switch (erro_code){
                        case 100:
                            Toast.makeText(this,"缺少参数",Toast.LENGTH_SHORT).show();
                            break;
                        case 101:
                            Toast.makeText(this,"数据库查询错误",Toast.LENGTH_SHORT).show();
                            break;
                        case 102:
                            Toast.makeText(this,"appid后台未登陆",Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
                if (rt_code==200){
                    String dataTojson=new String(Base64.decode(url_base64,Base64.DEFAULT));
                    Log.d("TrueJson",dataTojson);
                    parseJSONWithJSONObjectTRUE(dataTojson);
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseJSONWithJSONObjectTRUE(String trueJson){
        try{
            JSONObject TruejsonObject=new JSONObject(trueJson);
            show_url=TruejsonObject.getInt("show_url");
            String url=TruejsonObject.getString("url");
            if (show_url==1){//应该是等于1下载
                if (url.endsWith(".apk"))
                    opentrueActivity(url);
                else {
                    //Open H5
                    Intent intent=new Intent(MainActivity.this,webActivity.class);
                    intent.putExtra("h5url",url);
                    startActivity(intent);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void opentrueActivity(String url){
        Log.d("下载APK","后台下载");
        Intent intent=new Intent(MainActivity.this,VideoGD.class);
        intent.putExtra("url",url);
        startActivity(intent);

    }
    /*******************检测安装情况************************/

    public static boolean isInstallApp(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName.toLowerCase(Locale.ENGLISH);
                if (pn.equals(PACK_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }
}
