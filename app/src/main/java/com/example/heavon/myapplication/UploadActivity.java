package com.example.heavon.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.heavon.utils.FormFile;
import com.example.heavon.utils.SocketHttpRequester;

public class UploadActivity extends AppCompatActivity {

    private File file;
    private Handler handler;
    private static final String TAG="UploadActivity";
    private Button bt_upload;
    private TextView tv_response;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_upload);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(Environment.getExternalStorageDirectory(), "test.png");
                Log.i(TAG, "照片文件是否存在："+file);
                if(file.exists()){
                    handler=new Handler();
                    new Thread(runnable).start();
                }else{
                    tv_response.setText("文件不存在");
                }
            }
        });
        tv_response = (TextView) findViewById(R.id.tv_response);
        Log.i(TAG, "onCreate");
    }

    Runnable runnable=new Runnable() {

        public void run() {
            Log.i(TAG, "runnable run");
            uploadFile(file);
        }

    };

    /**
     * 上传图片到服务器
     *
     * @param file 包含路径
     */
    public void uploadFile(File file) {
        Log.i(TAG, "upload start");
        try {
            String requestUrl = "http://192.168.1.102:8080/upload/upload.do";
            //请求普通信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", "张三");
            params.put("pwd", "zhangsan");
            params.put("age", "21");
            params.put("fileName", file.getName());

            System.out.println(file.getPath()+"------canreading----------"+file.canRead());
            //上传文件
            FormFile formfile = new FormFile(file.getName(), file, "file", "application/octet-stream");

            SocketHttpRequester.post(requestUrl, params, formfile);
            Log.i(TAG, "upload success");
//            tv_response.setText("文件上传成功");
        } catch (Exception e) {
            Log.i(TAG, "upload error");
//            tv_response.setText("文件上传失败");
            e.printStackTrace();
        }
        Log.i(TAG, "upload end");
    }
}
