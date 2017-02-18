package com.example.heavon.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.smssdk.SMSSDK;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this, "19accee5f0490", "96545bc0dfb6f782602717bb863a9464");
        initWindowStatusBar();
    }

    //初始化顶部工具栏
    public void initToolBar(String title){
        System.out.println("startInitToolbar");
        /**开始初始化**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if(toolbar == null){
            return;
        }
        setSupportActionBar(toolbar);
        //设置标题栏
        setTitle("");
        if(tvTitle == null){
            setTitle(title);
        }else{
            tvTitle.setText(title);
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicActivity.this.finish();
            }
        });
        /**结束初始化**/
        System.out.println("endInitToolbar");
    }

    //初始化沉浸式系统状态栏
    public void initWindowStatusBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转到主页面
    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    //跳转到登录页面
    public void gotoLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    //跳转到登录页面
    public void enterSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        this.startActivity(intent);
    }
}
