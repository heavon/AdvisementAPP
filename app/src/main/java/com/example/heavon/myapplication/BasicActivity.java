package com.example.heavon.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.smssdk.SMSSDK;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.initSDK(this, "19accee5f0490", "96545bc0dfb6f782602717bb863a9464");
    }

    //初始化顶部工具栏
    public void initToolBar(String title){
        System.out.println("startInitToolbar");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(toolbar);
        setTitle(title);
        toolbar.setNavigationIcon(R.drawable.smssdk_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicActivity.this.finish();
            }
        });
        System.out.println("endInitToolbar");
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

}
